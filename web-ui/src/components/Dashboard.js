import React, { useEffect, useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import Histogram from './Histogram';
import LineChart from './LineChart';
import DonutChart from './DonutChart';
import { fetchDashboardConfig, executeQueries } from '../services/api';

const Dashboard = ({ dashboardName }) => {
  const [config, setConfig] = useState(null);
  const [queryResults, setQueryResults] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const dashboardConfig = await fetchDashboardConfig(dashboardName);
        setConfig(dashboardConfig);
        
        // Extract all query IDs from the dashboard
        const queryIds = dashboardConfig.grid.flatMap(row => 
          row.flatMap(col => col.queries?.map(q => q.id) || [])
        );
        
        if (queryIds.length > 0) {
          const results = await executeQueries(queryIds);
          setQueryResults(results);
        }
      } catch (error) {
        console.error('Error loading dashboard:', error);
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, [dashboardName]);

  if (loading) return <div>Loading dashboard...</div>;
  if (!config) return <div>Dashboard not found</div>;

  return (
    <Container>
      <h1 className="my-4">{config.name}</h1>
      {config.grid.map((row, rowIndex) => (
        <Row key={rowIndex} className="mb-4">
          {row.map((col, colIndex) => (
            <Col key={colIndex} md={12 / row.length}>
              {col.type === 'HISTOGRAM' && (
                <Histogram 
                  metadata={col}
                  data={queryResults}
                />
              )}
              {col.type === 'LINE_CHART' && (
                <LineChart 
                  metadata={col}
                  data={queryResults}
                />
              )}
              {col.type === 'DONUT_CHART' && (
                <DonutChart 
                  metadata={col}
                  data={queryResults}
                />
              )}
            </Col>
          ))}
        </Row>
      ))}
    </Container>
  );
};

export default Dashboard;

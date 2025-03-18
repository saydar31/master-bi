import React, { useEffect, useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import Histogram from './Histogram';
import LineChart from './LineChart';

const Dashboard = () => {
  const [config, setConfig] = useState(null);

  useEffect(() => {
    fetch('/dashboardConfig.json')
      .then((response) => response.json())
      .then((data) => setConfig(data));
  }, []);

  if (!config) return <div>Loading...</div>;

  return (
    <Container>
      <h1 className="my-4">{config.name}</h1>
      {config.grid.map((row, rowIndex) => (
        <Row key={rowIndex} className="mb-4">
          {row.map((col, colIndex) => (
            <Col key={colIndex}>
              {col.type === 'HISTOGRAM' && <Histogram title={col.name} />}
              {col.type === 'LINE_CHART' && <LineChart title={col.name} />}
            </Col>
          ))}
        </Row>
      ))}
    </Container>
  );
};

export default Dashboard;

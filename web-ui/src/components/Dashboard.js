import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import Histogram from './Histogram';
import LineChart from './LineChart';

const Dashboard = ({ config }) => {
    return (
        <Container>
            <Row>
                {config.grid.map((row, rowIndex) => (
                    <Col key={rowIndex}>
                        {row.map((chartConfig, index) => {
                            switch (chartConfig.type) {
                                case 'HISTOGRAM':
                                    return <Histogram key={index} config={chartConfig} />;
                                case 'LINE_CHART':
                                    return <LineChart key={index} config={chartConfig} />;
                                default:
                                    return null;
                            }
                        })}
                    </Col>
                ))}
            </Row>
        </Container>
    );
};

export default Dashboard;

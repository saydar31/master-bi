import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';


const Dashboard = ({ config }) => {
    return (
        <Container>
            <Row>
                {config.grid.map((row, rowIndex) => (
                    <Col key={rowIndex}>
                        {row.map((chartConfig, index) => {
                            return null;
                        })}
                    </Col>
                ))}
            </Row>
        </Container>
    );
};

export default Dashboard;

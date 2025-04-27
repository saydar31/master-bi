import React from 'react';
import { Card } from 'react-bootstrap';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const LineChart = ({ metadata, data }) => {
  const chartData = {
    labels: data.map((_, index) => index),
    datasets: [
      {
        label: 'Value',
        data: data.map(item => parseFloat(item.value)),
        borderColor: 'rgb(75, 192, 192)',
        backgroundColor: 'rgba(75, 192, 192, 0.5)',
        tension: 0.1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: metadata.name,
      },
    },
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  return (
    <Card>
      <Card.Header>{metadata.name}</Card.Header>
      <Card.Body>
        <Line data={chartData} options={options} />
      </Card.Body>
    </Card>
  );
};

export default LineChart;
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
  const queryIds = metadata.queries.map(q => q.id)
  const labels = queryIds.flatMap(e => data[e].data)
    .map(e => e.key)
  const chartData = {
    labels: labels,
    datasets: metadata.queries.map(item => {
      const queryData = data[item.id].data || []
      return {
        label: item.label || 'Value',
        data: queryData.map(e => parseFloat(e.value)),
        backgroundColor: item.visualizationProps.color || 'rgba(75, 192, 192, 0.5)',
        borderColor: item.visualizationProps.color || 'rgba(75, 192, 192, 1)',
        pointStyle: item.visualizationProps.shape || 'circle',
        tension: 0.1
      }
    }),
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
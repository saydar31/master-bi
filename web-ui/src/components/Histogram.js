import React from 'react';
import { Card } from 'react-bootstrap';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const Histogram = ({ metadata, data }) => {
  const queryIds = metadata.queries.map(q => q.id)
  const labels = queryIds.flatMap(e => data[e].data)
    .map(e => e.key)

  const chartData = {
    labels: labels,
    datasets: metadata.queries.map(item => {
      const queryData = data[item.id].data || []
      return {
        label: item.label || 'Value',
        data: queryData.map(e => ({x: e.key,  y: parseFloat(e.value)})),
        backgroundColor: item.visualizationProps.backgroundColor || 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      }
    })
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
        <Bar data={chartData} options={options} />
      </Card.Body>
    </Card>
  );
};

export default Histogram;
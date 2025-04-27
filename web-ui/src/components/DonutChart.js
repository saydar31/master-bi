import React from 'react';
import { Card } from 'react-bootstrap';
import { Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  Colors
} from 'chart.js';

// Register ChartJS components
ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  Colors
);

const DonutChart = ({ metadata, data }) => {
  // Transform query results to Chart.js format
  const chartData = {
    labels: data.map(item => item.key),
    datasets: [
      {
        data: data.map(item => parseFloat(item.value)),
        borderWidth: 1,
        cutout: '70%', // Makes it a donut instead of pie
      }
    ]
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'right',
      },
      title: {
        display: true,
        text: metadata.name,
        font: {
          size: 16
        }
      },
      // Customize the appearance
      tooltip: {
        callbacks: {
          label: function(context) {
            const label = context.label || '';
            const value = context.raw || 0;
            const total = context.dataset.data.reduce((a, b) => a + b, 0);
            const percentage = Math.round((value / total) * 100);
            return `${label}: ${value} (${percentage}%)`;
          }
        }
      }
    },
    // Add animation
    animation: {
      animateScale: true,
      animateRotate: true
    }
  };

  return (
    <Card>
      <Card.Header>{metadata.name}</Card.Header>
      <Card.Body style={{ position: 'relative', height: '400px' }}>
        <Doughnut data={chartData} options={options} />
      </Card.Body>
    </Card>
  );
};

export default DonutChart;

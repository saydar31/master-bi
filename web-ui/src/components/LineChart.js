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

const nameToDash = {
  'SOLID': [],
  'DOTTED': [5, 5],
  'DASHED': [10, 5] 
}

const LineChart = ({ metadata, data }) => {
  const queryIds = metadata.queries.map(q => q.id)
  const labels = [...new Set(
    queryIds.flatMap(e => 
      data[e]?.data?.map(item => item.key) || []
    )
  )];
  const chartData = {
    labels: labels,
    datasets: metadata.queries.map(item => {
      const queryData = data[item.id].data || []
      const borderDash = nameToDash[item.visualizationProps.lineType] || []
      return {
        label: item.label || 'Value',
        data: queryData.map(e => ({x: e.key,  y: parseFloat(e.value)})),
        backgroundColor: item.visualizationProps.backgroundColor || 'rgba(75, 192, 192, 0.5)',
        borderColor: item.visualizationProps.color || 'rgba(75, 192, 192, 1)',
        pointStyle: item.visualizationProps.shape || 'circle',
        borderDash: borderDash,
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
      x: {
        title: metadata.abscissa.displayName ? {
          display: true,
          text: metadata.abscissa.displayName
        } : undefined,
      },
      y: {
        title: metadata.ordinate.displayName ? {
          display: true,
          text: metadata.ordinate.displayName
        } : undefined,
        beginAtZero: true
      }
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
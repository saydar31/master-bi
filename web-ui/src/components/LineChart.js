import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';

// Регистрация всех компонентов Chart.js
Chart.register(...registerables);

const LineChart = ({ config }) => {
    const data = {
        labels: ['Label1', 'Label2', 'Label3'], // Замените на реальные данные
        datasets: [
            {
                label: config.name,
                data: [65, 59, 80], // Замените на реальные данные
                fill: false,
                backgroundColor: 'rgba(75, 192, 192, 1)',
                borderColor: 'rgba(75, 192, 192, 1)',
            },
        ],
    };

    return <Line data={data} />;
};

export default LineChart;

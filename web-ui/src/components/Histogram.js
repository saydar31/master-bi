import React from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';

// Регистрация всех компонентов Chart.js
Chart.register(...registerables);


const Histogram = ({ config }) => {
    const data = {
        labels: ['Label1', 'Label2', 'Label3'], // Замените на реальные данные
        datasets: [
            {
                label: config.name,
                data: [12, 19, 3], // Замените на реальные данные
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
            },
        ],
    };

    return <Bar data={data} />;
};

export default Histogram;

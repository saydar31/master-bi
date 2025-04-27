import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: false,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
  }
});

export const fetchDashboardConfig = async (name) => {
  const response = await api.get(`/dashboards/${name}`);
  return response.data;
};

export const executeQueries = async (queryIds) => {
  const response = await api.post('/data', {
    queryIds: queryIds
  });
  return response.data.idToResult;
};
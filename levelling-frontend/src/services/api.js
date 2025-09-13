import axios from 'axios';

const api = axios.create({
    baseURL: '/api/v1', // The base URL for all our API calls
});

// Axios Interceptor: This is magic!
// This function will run before every single request is sent.
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;
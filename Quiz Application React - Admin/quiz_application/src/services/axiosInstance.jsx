import axios from "axios";
import { useContext } from "react";
import { AuthContext } from "../components/auth/AuthContext";

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8765', // Your API Gateway base URL
});


// Add token from localStorage to Authorization header
// axiosInstance.interceptors.request.use(
//   (config) => {
//     const token = localStorage.getItem('token'); 
//     if (token) {
//       config.headers['Authorization'] = `Bearer ${token}`;
//     }
//     console.log(config);
//     return config;
//   },
//   (error) => Promise.reject(error)
// );

// Handle 401 responses globally (optional)
// axiosInstance.interceptors.response.use(
//   (response) => response,
//   (error) => {
//     if (error.response?.status === 401) {
//       console.warn('Unauthorized! Redirecting to login...');
//       // localStorage.removeItem('token');
//       window.dispatchEvent(new Event("forceLogout"));
//     }
//     return Promise.reject(error);
//   }
// );

export default axiosInstance;
import axios from 'axios';

const AUTH_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_AUTH_SERVICE;

export const login = (loginRequest) => axios.post(`${AUTH_BASE_URL}/login`, loginRequest,{
    headers:{
          'Content-Type':'application/json'
     },withCredentials: true});
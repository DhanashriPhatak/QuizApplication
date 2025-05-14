import axios from 'axios';

const QUIZ_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_QUIZ_SERVICE;

export const generateQuiz = (formData)=>axios.post(`${QUIZ_BASE_URL}/generateQuiz`,formData,{
    headers:{
          'Content-Type':'application/json'
     },withCredentials:true});

export const generateQuizManual = (formData) => axios.post(`${QUIZ_BASE_URL}/generateQuizManual`,formData,{
    headers:{
        'Content-Type':'application/json'
    },withCredentials:true});

export const getQuizQuestions = (id) => axios.post(`${QUIZ_BASE_URL}/get/${id}`, {withCredentials: true});
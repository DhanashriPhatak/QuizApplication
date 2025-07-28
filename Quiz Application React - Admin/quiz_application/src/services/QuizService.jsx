import axios from './axiosInstance';

const QUIZ_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_QUIZ_SERVICE;

export const generateQuiz = (formData)=>axios.post(`${QUIZ_BASE_URL}/generateQuiz`,formData,{
    headers:{
          'Content-Type':'application/json'
     }});

export const generateQuizManual = (formData) => axios.post(`${QUIZ_BASE_URL}/generateQuizManual`,formData,{
    headers:{
        'Content-Type':'application/json'
    }});

export const getQuizQuestions = (id) => axios.get(`${QUIZ_BASE_URL}/getQuestionPreview/${id}`);
export const getActiveInactiveCount = () => axios.get(`${QUIZ_BASE_URL}/getActiveInactiveCount`);
export const getPaginatedQuizzes = (isActive,page=0,size=10)=>axios.get(`${QUIZ_BASE_URL}/quizList`,{
    params:{
        isActive,page,size
    }});
export const getQuizDetails = (id)=>axios.get(`${QUIZ_BASE_URL}/view/${id}`);
export const updateQuiz = (formData)=>axios.post(`${QUIZ_BASE_URL}/update`,formData,{
    headers:{
        'Content-Type':'application/json'
    }});
export const updateQuizManual = (formData)=>axios.post(`${QUIZ_BASE_URL}/update/manual`,formData,{
    headers:{
        'Content-Type':'application/json'
    }});
export const deleteQuiz = (id)=>axios.delete(`${QUIZ_BASE_URL}/delete/${id}`);
export const getQuizHistory = (quizId)=>axios.get(`${QUIZ_BASE_URL}/${quizId}/history`);
export const activateQuizVersion = (quizId)=>axios.put(`${QUIZ_BASE_URL}/activate/${quizId}`);
export const deactivateQuiz = (quizId)=>axios.put(`${QUIZ_BASE_URL}/deactivate/${quizId}`);
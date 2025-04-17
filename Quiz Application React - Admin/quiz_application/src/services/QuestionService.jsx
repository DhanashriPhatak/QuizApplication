import axios from 'axios';

const QUESTION_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_QUESTION_SERVICE;


export const getAllQuestions = () => axios.get(`${QUESTION_BASE_URL}/getAllQuestions`, {withCredentials: true});
export const getAllCategories = () => axios.get(`${QUESTION_BASE_URL}/getAllCategories`, {withCredentials: true});
export const getQuestionsByCategory = (categoryId) => axios.get(`${QUESTION_BASE_URL}/getQuestionByCategory/${categoryId}`,
     {withCredentials: true});
// console.log("QUESTION_BASE_URL:", getAllQuestions().then(res));
// export const addQuestion = (question) => axios.post(`${BASE_URL}/add`, question);

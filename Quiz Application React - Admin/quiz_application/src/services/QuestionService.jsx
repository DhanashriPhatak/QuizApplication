import axios from 'axios';

const QUESTION_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_QUESTION_SERVICE;
const CATEGORY_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_CATEGORY;

export const getAllQuestions = () => axios.get(`${QUESTION_BASE_URL}/getAllQuestions`, {withCredentials: true});
export const getAllCategories = () => axios.get(`${CATEGORY_BASE_URL}/getAllCategories`, {withCredentials: true});
export const getQuestionsByCategory = (categoryId) => axios.get(`${QUESTION_BASE_URL}/getQuestionByCategory/${categoryId}`,
     {withCredentials: true});
export const getCategoryStats = ()=>axios.get(`${CATEGORY_BASE_URL}/getCategoryStats`,{withCredentials:true});
export const addCategory = (category)=>
     axios.post(`${CATEGORY_BASE_URL}/add`,category,{
          headers:{
               'Content-Type' :'application/json'
          }
     ,
     withCredentials:true});
// console.log("CATEGORY_BASE_URL:",CATEGORY_BASE_URL);
// export const addQuestion = (question) => axios.post(`${BASE_URL}/add`, question);

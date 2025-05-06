import axios from 'axios';

const QUESTION_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_QUESTION_SERVICE;
const CATEGORY_BASE_URL = process.env.REACT_APP_API_BASE_URL+process.env.REACT_APP_API_CATEGORY;

/** Question Rest Endpoints */
export const getAllQuestions = () => axios.get(`${QUESTION_BASE_URL}/getAllQuestions`, {withCredentials: true});
export const getQuestionsByCategory = (categoryId) => axios.get(`${QUESTION_BASE_URL}/getQuestionByCategory/${categoryId}`,
     {withCredentials: true});
export const addQuestion = (question)=>axios.post(`${QUESTION_BASE_URL}/add`,question,{
     headers:{
          'Content-Type':'application/json'
     },
     withCredentials:true});
export const editQuestion = (question)=>axios.post(`${QUESTION_BASE_URL}/edit`,question,{
     headers:{
          'Content-Type':'application/json'
     },
     withCredentials:true});
export const toggleQuestion = (id)=> axios.put(`${QUESTION_BASE_URL}/toggle/${id}`,{withCredentials:true});
export const deleteQuestion = (id)=> axios.delete(`${QUESTION_BASE_URL}/${id}`,{withCredentials:true});

/** Category Rest Endpoints */
export const getAllCategories = () => axios.get(`${CATEGORY_BASE_URL}/getAllCategories`, {withCredentials: true});
export const getCategoryStats = ()=>axios.get(`${CATEGORY_BASE_URL}/getCategoryStats`,{withCredentials:true});
export const addCategory = (category)=>
     axios.post(`${CATEGORY_BASE_URL}/add`,category,{
          headers:{
               'Content-Type' :'application/json'
          },
     withCredentials:true});
export const deleteCategory = (categoryId)=> axios.delete(`${CATEGORY_BASE_URL}/${categoryId}`, {withCredentials:true});
export const updateCategory = (id,category)=> axios.put(`${CATEGORY_BASE_URL}/${id}`, category,{
     headers:{
          'Content-Type':'application/json'
     },
     withCredentials:true});

console.log("getAllCategories.:-",CATEGORY_BASE_URL);

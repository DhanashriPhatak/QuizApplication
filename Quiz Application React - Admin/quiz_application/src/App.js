import './App.css';
import React, { useEffect } from 'react';
import AppRoutes from './routes/AppRoutes';
import { AuthProvider } from './components/auth/AuthContext';
import { BrowserRouter } from 'react-router-dom';


const App = () => {
  
  return <AppRoutes />;
};

export default App;


// const router = createBrowserRouter(
  //   createRoutesFromElements(
  //     <Route path='/' element={<MainLayout></MainLayout>}>
  //       <Route index element={<DashboardPage></DashboardPage>}></Route>
  //       <Route path='/generateQuiz' element={<GenerateQuizPage></GenerateQuizPage>}></Route>
  //       <Route path='/quizHistory' element={<QuizHistoryPage></QuizHistoryPage>}></Route>
  //       <Route path='/questions' element={<QuestionsPage></QuestionsPage>}></Route>
  //       {/* <Route path='*' element={<NotFoundPage></NotFoundPage>}></Route> */}
  //     </Route>
  // )
  // );
//   <BrowserRouter>
//   <AppRoutes />
// </BrowserRouter>

  // return  <RouterProvider router={router}></RouterProvider>
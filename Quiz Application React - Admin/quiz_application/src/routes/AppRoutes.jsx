import { BrowserRouter, Routes, Route,Navigate  } from 'react-router-dom';
import MainLayout from '../layout/MainLayout';
import DashboardPage from '../pages/DashboardPage';
import QuestionsPage from '../pages/QuestionsPage';
import NotFoundPage from '../pages/NotFoundPage';
import GenerateQuizPage from '../pages/GenerateQuizPage';
import CategoriesPage from '../pages/CategoriesPage';
import QuizPage from '../pages/QuizPage';
import ViewQuizPage from '../pages/ViewQuizPage';
import AdminLoginPage from '../pages/AdminLoginPage';
import { useContext } from 'react';
import { AuthContext } from '../components/auth/AuthContext';



const AppRoutes = () => {
  // const {isAuthenticated} = useContext(AuthContext);
  // console.log("isAuthenticated:-"+isAuthenticated);
    return (
    <>
      <Routes>
        <Route index path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<AdminLoginPage />} />
        {/* {isAuthenticated && ( */}
          <Route path="/" element={<MainLayout />}>
            <Route path="home" element={<DashboardPage />} />
            <Route path="generateQuiz/:id" element={<GenerateQuizPage />} />
            <Route path="generateQuiz" element={<GenerateQuizPage />} />
            <Route path="quiz" element={<QuizPage />} />
            <Route path="quiz/view/:quizId" element={<ViewQuizPage />} />
            <Route path="questions" element={<QuestionsPage />} />
            <Route path="categories" element={<CategoriesPage />} />
            <Route path="users" element={<NotFoundPage />} />
            <Route path="*" element={<NotFoundPage />} />
          </Route>
        {/* )} */}
      </Routes>
    </>
);
};

export default AppRoutes;
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from '../layout/MainLayout';
import DashboardPage from '../pages/DashboardPage';
import QuestionsPage from '../pages/QuestionsPage';
import NotFoundPage from '../pages/NotFoundPage';
import GenerateQuizPage from '../pages/GenerateQuizPage';
import CategoriesPage from '../pages/CategoriesPage';
import QuizPage from '../pages/QuizPage';

const AppRoutes = () => {
    return (
<BrowserRouter>
    <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<NotFoundPage />} /> {/* DashbaordPage*/}
          <Route path="generateQuiz" element={<GenerateQuizPage />} />
          <Route path="quiz" element={<QuizPage />} />
          <Route path="questions" element={<QuestionsPage />} />
          <Route path="categories" element={<CategoriesPage/>} />
          <Route path="users" element={<NotFoundPage/>}/>
          <Route path="*" element={<NotFoundPage />} />
        </Route>
      </Routes>
</BrowserRouter>
);
};

export default AppRoutes;
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from '../layout/MainLayout';
import DashboardPage from '../pages/DashboardPage';
import QuestionsPage from '../pages/QuestionsPage';
import AddEditQuestionPage from '../pages/AddEditQuestionPage';
import NotFoundPage from '../pages/NotFoundPage';
import GenerateQuizPage from '../pages/GenerateQuizPage';

const AppRoutes = () => {
    return (
<BrowserRouter>
    <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="generateQuiz" element={<GenerateQuizPage />} />
          <Route path="questions" element={<QuestionsPage />} />
          <Route path="questions/add" element={<AddEditQuestionPage mode="add" />} />
          <Route path="questions/edit/:id" element={<AddEditQuestionPage mode="edit" />} />
          <Route path="*" element={<NotFoundPage />} />
        </Route>
      </Routes>
</BrowserRouter>
);
};

export default AppRoutes;
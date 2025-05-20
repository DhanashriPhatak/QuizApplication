import React, { useState } from 'react';
import QuizPreviewPanel from '../components/quiz/QuizPreviewPanel';
import GenerateQuizForm from '../components/quiz/GenerateQuizForm';

const GenerateQuizPage = () => {
  const [quizId,setQuizId] = useState(null);
  return (
    <>
    <main className="app-main">
      <div className="app-content-header">
        <div className="container-fluid px-4 py-3">
          <div className="row align-items-start">
            <div className="col-lg-6 col-md-12 mb-4">
              <h3 className="mb-3 fw-bold">Generate Quiz</h3>
              <GenerateQuizForm onPreviewUpdate={setQuizId}/>
            </div>
            <div className="col-lg-6 col-md-12">
              <h3 className="mb-3 fw-bold">Quiz Preview</h3>
              <QuizPreviewPanel quizId={quizId} previewMode={true}/>
            </div>
          </div>
        </div>
      </div>
    </main>
    </>
  )
}

export default GenerateQuizPage;
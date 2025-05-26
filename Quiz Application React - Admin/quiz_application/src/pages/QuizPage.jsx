import React from 'react';
import Quiz from '../components/quiz/Quiz';

const QuizPage = () => {
  return (
    <>
      <main className="app-main">
        <div className="app-content-header">
          <div className="container-fluid px-4 py-3">
            <div className="row align-items-start">
              <div className="col-lg-12 col-md-12 mb-4">
                <Quiz></Quiz>
              </div>
            </div>
          </div>
        </div>
      </main>
      
    </>
  )
}

export default QuizPage
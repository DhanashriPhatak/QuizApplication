import React from 'react'
import { useParams } from 'react-router-dom'
import ViewQuiz from '../components/quiz/ViewQuiz';

const ViewQuizPage = () => {
    const {id} = useParams();
  return (
    <>
      <main className="app-main">
      <div className="app-content-header">
        <div className="container-fluid px-4 py-3">
          <div className="row align-items-start">
            <div className="col-lg-12 col-md-12 mb-4">
              <ViewQuiz quizId={id} />
            </div>
          </div>
        </div>
      </div>
    </main>
    </>
  )
}

export default ViewQuizPage
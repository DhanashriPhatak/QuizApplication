import React from 'react'
import { useParams } from 'react-router-dom'
import ViewQuiz from '../components/quiz/ViewQuiz';

const ViewQuizPage = () => {
    const {id} = useParams();
  return (
    <ViewQuiz quizId={id} />
  )
}

export default ViewQuizPage
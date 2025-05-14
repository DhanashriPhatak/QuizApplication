import React, { useEffect, useState } from 'react'
import { getQuizQuestions } from '../../services/QuizService';

const QuizPreviewPanel = ({quizId}) => {
  const [questions,setQuestions] = useState([]);
  const [loading,setLoading] = useState(false);
  const [error,setError] = useState('');

  useEffect(()=>{
    if(!quizId) return;

    setLoading(true);
    setError('');

    getQuizQuestions(quizId)
    .then((res)=>{
        setQuestions(res.data);
        setError('');
    })
    .catch((error)=>{
        setError("Failed to fetch Questions. Try Again");
    })
    .finally(()=>{
        setLoading(false);
    })
  },[quizId]);

  if(!quizId)
  {
    return <div>Select options and Generate Quiz Preview</div>
  }
  if(loading)
  {
    return <div>Loading preview...</div>
  }
  if(error)
  {
    return <div className="text-danger">{error}</div>
  }

  return (
    <>
       <div className="quiz-preview">
          {questions.map((q, index) => (
            <div key={index} className="mb-3 p-3 border rounded bg-light">
              <h6>{index + 1}. {q.question}</h6>
              <ul>
                <li>A. {q.optionA}</li>
                <li>B. {q.optionB}</li>
                <li>C. {q.optionC}</li>
                <li>D. {q.optionD}</li>
              </ul>
            </div>
          ))}
        </div>
    </>
  )
}

export default QuizPreviewPanel
import React, { useEffect, useState } from 'react'
import { getQuizQuestions } from '../../services/QuizService';

const QuizPreviewPanel = ({quizId,previewMode=false}) => {
  const [questions,setQuestions] = useState([]);
  const [loading,setLoading] = useState(false);
  const [error,setError] = useState('');

  useEffect(()=>{
    if(!quizId) return;

    setLoading(true);
    setError('');

    getQuizQuestions(quizId)
    .then((res)=>{
        const transformed = (res.data || []).map(q=>({
          id:q.id,
          text:q.question,
          category:q.category,
          difficulty:q.diff_level,
          options:[q.option_1,q.option_2,q.option_3,q.option_4]
        }));
        setQuestions(transformed);
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
          {questions.map((question, index) => (
            <div key={question.id} className="mb-4 p-3 border rounded">
              <h5>Q{index + 1}. {question.text || 'Untitled Question'}</h5>

              {previewMode && (
                <p className="text-muted">
                  <strong>Category:</strong> {question.category || 'N/A'} | <strong>Difficulty:</strong> {question.difficulty || 'N/A' }
                </p>
              )}

              <ul>
                {(question.options || []).map((opt, idx) => (
                  <li key={idx}>{opt}</li>
                ))}
              </ul>
            </div>
          ))}
        </div>
    </>
  )
}

export default QuizPreviewPanel
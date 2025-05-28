import React, { useEffect, useState } from 'react';
import Spinner from '../common/Spinner';
import { getQuizDetails } from '../../services/QuizService';
import { useNavigate } from 'react-router-dom';

const ViewQuiz = ({quizId}) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const [quiz,setQuiz] = useState(null);

    useEffect(()=>{
        if(!quizId) {
            setError("NO Quiz id to show the quiz");
            return;
        };
        setLoading(true);
        getQuizDetails(quizId)
        .then(res=>{
            setQuiz(res.data);
            setError('');
        })
        .catch(err=>{
            setError('Failed to load quiz Details');
        })
        .finally(()=>{
            setLoading(false);
        })
    },[quizId]);

    if (loading) return <Spinner />;
    if (error) return <div className="alert alert-danger">{error}</div>;
    if (!quiz) return null;

    return (
    <>
        <div className="card card-info card-outline">
             <div className="card-header d-flex justify-content-between align-items-center">
                <button 
                className="btn btn-outline-secondary btn-sm" 
                onClick={() => navigate('/quiz')}
                >
                ← Back
                </button>
                
                <h4 className="card-title mb-0">
                <span className="fw-bold me-2">Quiz Title:</span> {quiz.quizTitle}
                <div className="text-muted small">Created: {new Date(quiz.createdAt).toLocaleString()}</div>
                </h4>
            </div>

            <div className="card-body">
                <h5>Category-wise Breakdown</h5>
                <ul>
                {quiz.categoryDifficultyPairLsit.map((item, idx) => (
                    <li key={idx}>
                    <strong>{item.categoryName}</strong> – {item.diffLevel} ({item.count} questions)
                    </li>
                ))}
                </ul>

                <hr />

                <h5>Questions</h5>
                {quiz.questionWrapperList.map((q, i) => (
                <div key={q.id} className="mb-3 border rounded p-2">
                    <strong>Q{i + 1}: {q.question}</strong>
                    <ul>
                    <li>{q.option_1}</li>
                    <li>{q.option_2}</li>
                    <li>{q.option_3}</li>
                    <li>{q.option_4}</li>
                    </ul>
                    <small className="text-muted">Category: {q.category} | Difficulty: {q.diff_level}</small>
                </div>
                ))}
            </div>
        </div>
    </>
    )
}

export default ViewQuiz
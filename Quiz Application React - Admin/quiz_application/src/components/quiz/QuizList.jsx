import React, { useEffect, useState } from 'react'
import Spinner from '../common/Spinner';
import { getPaginatedQuizzes } from '../../services/QuizService';
import { useNavigate } from 'react-router-dom';


const QuizList = ({status}) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [quizzes,setQuizzes] = useState([]);
  const [totalPages,setTotalPages] = useState(0);
  const [page,setPage] = useState(0);
  const [isActive,setIsActive] = useState();//toggle for active/inactive quizzes
  const navigate = useNavigate();

  useEffect(()=>{
    setLoading(true);
    getPaginatedQuizzes(status==="active",page)
    .then((res)=>{
      setQuizzes(res.data.content);
      setTotalPages(res.data.totalPages);
      setError('');
    })
    .catch((err)=>{
      console.log(err);
      setError("Failed to load Quizzes");
    })
    .finally(()=>{
      setLoading(false);
    })
  },[status,page]);

  const toggleQuizStatus = ()=>{
    
  }

  const handleEditQuiz = ()=>{
    
  }

  const handleDeleteQuiz = ()=>{
    
  }

  if (loading) return <Spinner />;
  return (
    <>
    {error &&  <div className="alert alert-danger">{error}</div>}
    <table className="table table-bordered table-striped">
      <thead>
        <tr>
          <th style={{ width: "10px" }}>#</th>
          <th>Quiz Title</th>
          <th>Created At</th>
          <th>Questions</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {quizzes.map((quiz, idx) => (
          <tr key={quiz.quiz_id}>
            <td>{idx + 1 + (page * 10)}</td>
            <td>{quiz.quiz_title}</td>
            <td>{new Date(quiz.createdAt).toLocaleString()}</td>
            <td>{quiz.questions.length}</td>
            <td>
              <div className="d-flex align-items-center gap-3">
                <div className="dropdown">
                  <button
                    className="btn btn-sm btn-secondary dropdown-toggle"
                    type="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    Actions
                  </button>
                  <ul className="dropdown-menu">
                    <li><button className="dropdown-item" onClick={()=>navigate(`/quiz/view/${quiz.quiz_id}`)}>View</button></li>
                    <li><button className="dropdown-item" onClick={()=>handleEditQuiz()}>Edit</button></li>
                    <li><button className="dropdown-item text-danger" onClick={()=>handleDeleteQuiz()}>Delete</button></li>
                  </ul>
                </div>
                <div className="form-check form-switch">
                  <input 
                    className="form-check-input" 
                    type="checkbox" 
                    checked={status==='active'}
                    onChange={() => toggleQuizStatus(quiz.quiz_id)} 
                  />
                </div>
              </div>
            </td>
          </tr>
        ))}
      </tbody>
    </table>

    <div className="card-footer clearfix">
      <ul className="pagination pagination-sm m-0 float-end">
        <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
          <button className="page-link" onClick={() => setPage(prev => Math.max(prev - 1, 0))}>&laquo;</button>
        </li>
        {Array.from({ length: totalPages }, (_, i) => (
          <li key={i} className={`page-item ${i === page ? 'active' : ''}`}>
            <button className="page-link" onClick={() => setPage(i)}>{i + 1}</button>
          </li>
        ))}
        <li className={`page-item ${page + 1 >= totalPages ? 'disabled' : ''}`}>
          <button className="page-link" onClick={() => setPage(prev => prev + 1)}>&raquo;</button>
        </li>
      </ul>
    </div>
    </>
  )
}

export default QuizList
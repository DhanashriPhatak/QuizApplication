import React, { useEffect, useState } from 'react'
import Spinner from '../common/Spinner';
import { getPaginatedQuizzes , deleteQuiz, getQuizHistory, activateQuizVersion, deactivateQuiz } from '../../services/QuizService';
import { useNavigate } from 'react-router-dom';
import ShowToast from '../common/ShowToast';
import ConfirmModal from '../common/ConfirmModal';

const QuizList = ({status,reloadTrigger,onReload}) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [quizzes,setQuizzes] = useState([]);
  const [totalPages,setTotalPages] = useState(0);
  const [page,setPage] = useState(0);
  const [isActive,setIsActive] = useState();//toggle for active/inactive quizzes
  const navigate = useNavigate();

  const loadQuizzes = ()=>{
     setLoading(true);
      getPaginatedQuizzes(status==="active",page)
      .then((res)=>{
        console.log("🚀 Loaded quizzes after activation:", res.data.content);
        setQuizzes(res.data.content);
        setTotalPages(res.data.totalPages);
        setError('');
      })
      .catch((err)=>{
        setError("Failed to load Quizzes");
      })
      .finally(()=>{
        setLoading(false);
      })
  }
  useEffect(()=>{
   loadQuizzes();
  },[status,page,reloadTrigger]);

  const toggleQuizStatus = (quizId)=>{
    if(status != "active") return;
    setLoading(true);
    deactivateQuiz(quizId)
    .then(()=>{
      ShowToast({type:'success',title:'Success',message:'Quiz marked as Inactivate'});
      loadQuizzes();
      if(onReload)onReload();
    })
    .catch((err)=>{
      console.log(err);
      ShowToast({ type: 'error', title: 'Error', message: 'Failed to update quiz status.' });
    })
    .finally(() => {
      setLoading(false);
    });
  }

  const handleEditQuiz = (quizId,mode)=>{
    navigate(`/generateQuiz/${quizId}?mode=${mode}`);
  }

  /**Start- Delete a quiz */
  const [showConfirmModal,setShowConfirmModal] = useState(false);
  const [deleteTargetId,setDeleteTargetId] = useState(null);
  const [deletingId,setDeletingId] = useState(null);
  const handleDeleteQuiz = (e,id)=>{
    e.stopPropagation();
    setDeleteTargetId(id);
    setShowConfirmModal(true);
  }
  const handleDeleteConfirmed = ()=>{
      setDeletingId(deleteTargetId);
      deleteQuiz(deleteTargetId)
      .then(()=>{
          if (onReload) onReload(); 
          // setQuizzes(prev =>prev.filter(q=>q.quiz_id!==deleteTargetId));
          ShowToast({type:'success',title:'Success',message:'Question deleted successfully.'})
      })
      .catch((error)=>{
          ShowToast({ type: 'error', title: 'Error', message: 'Failed to delete question. Try again.' });
      })
      .finally(() => {
          setDeletingId(null);
          setDeleteTargetId(null);
          setShowConfirmModal(false);
      });
  }
  /**End- Delete a quiz */
  /** Start- view quiz history */
  const [showHistoryModal,setShowHistoryModal] = useState(false);
  const [history, setHistory] = useState([]);
  const [selectedQuizTitle,setSelectedQuizTitle] = useState('');
  const handleViewHistory = (quizId,title)=>{
    setLoading(true);
    getQuizHistory(quizId)
    .then(res=>{
      setHistory(res.data);
      setSelectedQuizTitle(title);
      setShowHistoryModal(true);
    })
    .catch(err=>{
      ShowToast({ type: 'error', title: 'Error', message: 'Failed to fetch history. Try again.' });
    })
    .finally(()=>{
      setLoading(false);
    })
  }
  /** End- view Wuiz history */
  /**Start - activate from history */
  const [showConfirmActivateModal,setShowConfirmActivateModal] = useState(false);
  const [selectedVersionId,setSelectedVersionId] = useState(null);
  const [activating,setActivating] = useState(false);
  const handleActivateVersion = (quizId)=>{
    setSelectedVersionId(quizId);
    setShowConfirmActivateModal(true);
  }
  const handleConfirmActivateVersion = ()=>{
    setActivating(true);
    setLoading(true);
    activateQuizVersion(selectedVersionId)
    .then(()=>{
      ShowToast({type:'success',title:'Success',message:'Quiz version activated successfully.'});
      // setReloadTrigger(prev=>prev+1);
      // console.log("✅ Activation succeeded. Calling loadQuizzes()...");
      loadQuizzes();
      if(onReload)onReload();
      return getQuizHistory(selectedVersionId);
    })
    .then((res)=>{
      // console.log("📜 Updated history:", res.data);
      setHistory(res.data);
    })
    .catch((err)=>{
      ShowToast({type:'error',title:'Error',message:'Failed to activate version. Try again later.'});
    })
    .finally(()=>{
      setActivating(false);
      setLoading(false);
      setShowConfirmActivateModal(false);
    });
  };
  /**End - activate from history */

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
                    {status=== 'active' && (
                      <li><button className="dropdown-item" onClick={()=>handleEditQuiz(quiz.quiz_id,quiz.mode)}>Edit</button></li>
                    )}
                    <li><button className="dropdown-item text-danger" onClick={(e)=>handleDeleteQuiz(e,quiz.quiz_id)}>Delete</button></li>
                    <li><button className="dropdown-item " onClick={(e)=>handleViewHistory(quiz.quiz_id,quiz.quiz_title)}>View History</button></li>
                  </ul>
                </div>
                {status === 'active' && (
                  <div className="form-check form-switch">
                    <input 
                      className="form-check-input" 
                      type="checkbox" 
                      checked={status === 'active'}
                      onChange={() => toggleQuizStatus(quiz.quiz_id)} 
                    />
                  </div>
                )}
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
    {showConfirmModal && (
      <ConfirmModal
        title="Confirm Delete"
        message="Are you sure you want to delete this Quiz?"
        show={showConfirmModal}
        onClose={() => setShowConfirmModal(false)}
        onConfirm={handleDeleteConfirmed}
        confirmText="Yes, Delete"
        confirmBtnClass="btn-danger"
        loading={deletingId}
      />
    )}
    {showHistoryModal && (
      <div className="modal fade show d-block" tabIndex="-1" role="dialog" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
        <div className="modal-dialog modal-lg" role="document">
          <div className="modal-content">
            <div className="modal-header bg-primary text-white">
              <h5 className="modal-title">History of: {selectedQuizTitle}</h5>
              <button type="button" className="btn-close" onClick={() => setShowHistoryModal(false)}></button>
            </div>
            <div className="modal-body">
              {history.length === 0 ? (
                <p>No history available.</p>
              ) : (
                <table className="table table-bordered">
                  <thead>
                    <tr>
                      <th>Version</th>
                      <th>Created At</th>
                      <th>Mode</th>
                      <th>Title</th>
                      <th>Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {history.map((h, i) => (
                      <tr key={i}>
                        <td>{h.version}</td>
                        <td>{new Date(h.createdAt).toLocaleString()}</td>
                        <td>{h.mode}</td>
                        <td>{h.quiz_title}</td>
                        <td>{h.active ?(
                          <span className="badge bg-success">Active</span>
                        ):
                        (
                          <span className="badge bg-secondary">Inactive</span>
                        )}</td>
                        <td>
                          <div className="d-flex gap-2">
                            <button
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => window.open(`/quiz/view/${h.quiz_id}`, '_blank')}
                            >
                              View
                            </button>
                            {!h.active && (
                              <button
                                className="btn btn-sm btn-outline-success"
                                onClick={() => handleActivateVersion(h.quiz_id)}
                              >
                                Activate
                              </button>
                            )}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
            <div className="modal-footer">
              <button className="btn btn-secondary" onClick={() => setShowHistoryModal(false)}>Close</button>
            </div>
          </div>
        </div>
      </div>
    )}
    {showConfirmActivateModal && (
      <ConfirmModal
        title="Activate Quiz Version"
        message="This will deactivate the currently active version. Proceed?"
        show={showConfirmActivateModal}
        onClose={() => setShowConfirmActivateModal(false)}
        onConfirm={handleConfirmActivateVersion}
        confirmText="Yes, Activate"
        confirmBtnClass="btn-success"
        loading={activating}
      />
    )}
    </>
  )
}

export default QuizList
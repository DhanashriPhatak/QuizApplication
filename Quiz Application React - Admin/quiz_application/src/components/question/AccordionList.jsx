import React, { useState } from 'react';
import Switch from "react-switch";
import { toggleQuestion ,deleteQuestion} from '../../services/QuestionService';
import ShowToast from '../common/ShowToast';
import Spinner from '../common/Spinner';
import '../../css/accordionList.css';
import AddQuestionModal from './AddQuestionModal';

  const AccordionList = ({ questions = [] ,categoryId,categoryName,setQuestions}) => {
    const accordionId = `accordion-${categoryName.replace(/\s+/g,"-").toLowerCase()}`;
    /*
        /.../ -> delimiter that defines regex pattern
        /s -> Matches any whitespace character (space, tab, newline)
        + -> Matches one or more of the preceding token (in this case, whitespace)
        g -> Global flag - replaces all matches ,not just the first
    */
   /**Start - Toggle question Active/Inactice */
   const [updatingId,setUpdatingId] = useState(null);
    const handleToggle = (id,currentStatus)=>{
        if(updatingId)return;
        setUpdatingId(id);
        toggleQuestion(id)
        .then((res)=>{
            const updatedStatus = currentStatus === 1 ? 0 : 1;
            setQuestions(prevQuestions =>
                prevQuestions.map(q =>
                q.id === id ? { ...q, active: updatedStatus } : q
                )
            );
            // console.log("questions:-",questions);
            const status = currentStatus===0 ? 'ACTIVE' : 'INACTIVE';
            const msg = `Question marked as ${status}`;
            ShowToast({ type: 'success', title: 'Success', message: msg });
        })
        .catch((error)=>{
            let errorMsg = 'Failed to toggle the status of a Question. Please try again.';

            // Check for 409 Conflict
            if (error?.response?.status === 409) {
                errorMsg = error?.response?.data || 'This question is used in an active quiz and cannot be deactivated.';
            }
            ShowToast({type:'error',title:'Error',message:errorMsg});
        })
        .finally(() => {
            setUpdatingId(null);
        });
    }
    /**End - Toggle question Active/Inactice */
    /** start- edit question */
    const [showEditModal,setShowEditModal] = useState(false);
    const [questionToEdit,setQuestionToEdit] = useState(null);
    const handleEdit = (e,q)=>{
        e.stopPropagation();
        setQuestionToEdit(q);
        setShowEditModal(true);
    };
    const handleUpdateQuestion = (updatedQ)=>{
        setQuestions(prev=>prev.map(q=>q.id===updatedQ.id?{...updatedQ}:q));
    }
    /**End- edit question */
    /**Start - Delete Question */
    const[showConfirmModal,setShowConfirmModal] = useState(false);
    const [deleteTargetId,setDeleteTargetId] = useState(null);
    const [deletingId,setDeletingId] = useState(null);
    const handleDelete = (e,id) =>{
        e.stopPropagation();
        setDeleteTargetId(id);
        setShowConfirmModal(true);
    }
    const handleDeleteConfirmed = ()=>{
        setDeletingId(deleteTargetId);
        deleteQuestion(deleteTargetId)
        .then(()=>{
            setQuestions(prev =>prev.filter(q=>q.id!==deleteTargetId));
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
    /**End - Delete Question */
    return (
        <>
            <div className="accordion" id={accordionId}>
                {questions.map((q,index)=>{
                    // console.log(q);
                    const collapseId =  `collapse-${categoryId}-${index}`;
                    const headingId = `heading-${categoryId}-${index}`;
                    return (
                        <div className="accordion-item" key={q.id}>
                            <h2 className="accordion-header" id={headingId}> 
                                <div className="d-flex justify-content-between align-items-center w-100 px-3 py-2">
                                    <button className="accordion-button flex-grow-1 mb-0"
                                    type="button" data-bs-toggle="collapse" data-bs-target={`#${collapseId}`} 
                                    aria-expanded="true" aria-controls={collapseId}
                                    style={{ background: 'none', border: 'none', padding: 0 }}
                                    >
                                    <span className="text-start">{q.question}</span>
                                    </button>
                                        {/*For badge printing active/inactive & diff level */}
                                    <div className="d-flex align-items-center gap-3 ms-3">
                                        {updatingId === q.id ? (
                                        <Spinner /> 
                                        ) :
                                        (<span title={q.active ? "Active" : "Inactive"}>
                                        <Switch
                                            checked={q.active}
                                            onChange={() => handleToggle(q.id,q.active)}
                                            onColor="#00C851"
                                            offColor="#ff4444"
                                            uncheckedIcon={false}
                                            checkedIcon={false}
                                            height={18}   
                                            width={36} 
                                            />
                                        </span>
                                        )}
                                        <div className="dropdown">
                                            <button
                                                className="btn btn-sm btn-light border dropdown-toggle"
                                                type="button"
                                                data-bs-toggle="dropdown"
                                                aria-expanded="false"
                                                onClick={(e)=>e.stopPropagation()}
                                            >
                                                <i className="bi bi-gear-fill"></i>
                                            </button>
                                            <ul className="dropdown-menu">
                                                <li>
                                                <button className="dropdown-item" onClick={(e) => handleEdit(e,q)}>
                                                    <i className="bi bi-pencil-square me-2 text-primary"></i> Edit
                                                </button>
                                                </li>
                                                <li>
                                                <button className="dropdown-item text-danger" onClick={(e) => handleDelete(e,q.id)}>
                                                    <i className="bi bi-trash3 me-2"></i> Delete
                                                </button>
                                                </li>
                                            </ul>
                                        </div>
                                        <span className={`badge rounded-pill px-3 py-2 text-white fw-semibold shadow-sm ${
                                            q.diff_level === 'Easy' ? 'bg-success' :
                                            q.diff_level === 'Medium' ? 'bg-warning text-dark' :
                                            'bg-danger'
                                        }`}  style={{ fontSize: '0.75rem', padding: '0.35rem 0.6rem' }}>
                                            <i className="bi bi-lightning-charge-fill" style={{ fontSize: '0.85rem' }}></i>
                                            {q.diff_level}
                                        </span>
                                    </div>
                                    </div>
                                 </h2>
                            {/* To show your options and answer*/}
                            <div id={collapseId} className="accordion-collapse collapse" data-bs-parent={`#${accordionId}`}>
                                <div className="accordion-body"> 
                                    {[q.option_a,q.option_b,q.option_c,q.option_d].map((option,idx)=>{
                                        const optionNumber = idx+1;
                                        const isCorrect = option===q.ans;

                                        return(
                                            <div key={idx} className="d-flex justify-content-between align-items-center gap-2 py-1 px-2 border rounded mb-2"
                                            style={{backgroundColor:isCorrect?'#e6ffed' : 'transparent',}}
                                            >
                                                <span className="me-1">{optionNumber}</span>
                                                <span className="flex-grow-1">{option}</span>
                                                {isCorrect && (
                                                    <span className="text-success fs-5">
                                                        <i className="bi bi-check-circle-fill"></i>
                                                    </span>
                                                )}
                                            </div>
                                        )
                                    })}
                                </div>
                            </div>
                        </div>
                    )
                })}
                {showConfirmModal && (
                    <div className="modal fade show d-block" tabIndex="-1" role="dialog" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                        <div className="modal-dialog" role="document">
                            <div className="modal-content">

                                <div className="modal-header bg-danger text-white">
                                    <h5 className="modal-title">Confirm Delete</h5>
                                    <button type="button" className="btn-close" onClick={() => setShowConfirmModal(false)}></button>
                                </div>

                                <div className="modal-body">
                                    Are you sure you want to delete this question?
                                </div>

                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={() => setShowConfirmModal(false)}>Cancel</button>
                                    <button type="button" className="btn btn-danger" onClick={handleDeleteConfirmed}>
                                        {deletingId ? <Spinner /> : 'Yes, Delete'}
                                    </button>
                                </div>

                            </div>
                        </div>
                    </div>
                )}
                {showEditModal && (
                <AddQuestionModal
                    show={showEditModal}
                    onClose={() => setShowEditModal(false)}
                    editMode={true}
                    existingQuestion={questionToEdit}
                    onUpdate={(updatedQ) => {
                    setQuestions(prev =>
                        prev.map(q => q.id === updatedQ.id ? updatedQ : q)
                    );
                    }}
                />
                )}
            </div>
        </>
)}

export default React.memo(AccordionList);


{/* <div className="accordion-item">
<h2 className="accordion-header"> <button className="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
    Accordion Item #2
  </button> </h2>
<div id="collapseTwo" className="accordion-collapse collapse" data-bs-parent="#accordionExample">
  <div className="accordion-body"> <strong>This is the second item's accordion body.</strong> It is hidden by default, until the collapse plugin
    adds the appropriate classes that we use to style
    each element. These classes control the overall
    appearance, as well as the showing and hiding via
    CSS transitions. You can modify any of this with
    custom CSS or overriding our default variables. It's
    also worth noting that just about any HTML can go
    within the <code>.accordion-body</code>, though the
    transition does limit overflow.
  </div>
</div>
</div> */}
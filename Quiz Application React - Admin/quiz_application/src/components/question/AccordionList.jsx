import React, { useState } from 'react';
import Switch from "react-switch";
import { toggleQuestion } from '../../services/QuestionService';
import ShowToast from '../common/ShowToast';
import Spinner from '../common/Spinner';

  const AccordionList = ({ questions = [] ,categoryId,categoryName,setQuestions}) => {
    const accordionId = `accordion-${categoryName.replace(/\s+/g,"-").toLowerCase()}`;
    /*
        /.../ -> delimiter that defines regex pattern
        /s -> Matches any whitespace character (space, tab, newline)
        + -> Matches one or more of the preceding token (in this case, whitespace)
        g -> Global flag - replaces all matches ,not just the first
    */
   const [updatingId,setUpdatingId] = useState(null);
    const handleToggle = (id,currentStatus)=>{
        setUpdatingId(id);
        toggleQuestion(id)
        .then((res)=>{
            const updatedStatus = currentStatus === 1 ? 0 : 1;
            setQuestions(prevQuestions =>
                prevQuestions.map(q =>
                q.id === id ? { ...q, isActive: updatedStatus } : q
                )
            );
            console.log("questions:-",questions);
            const status = currentStatus===0 ? 'ACTIVE' : 'INACTIVE';
            const msg = `Question marked as ${status}`;
            ShowToast({ type: 'success', title: 'Success', message: msg });
        })
        .catch((error)=>{
            ShowToast({type:'error',title:'Error',message:'Failed to Toggle the status of a Question. Please try again.'});
        })
        .finally(() => {
            setUpdatingId(null);
        });
    }
    return (
        <>
            <div className="accordion" id={accordionId}>
                {questions.map((q,index)=>{
                    const collapseId =  `collapse-${categoryId}-${index}`;
                    const headingId = `heading-${categoryId}-${index}`;
                    return (
                        <div className="accordion-item" key={index}>
                            <h2 className="accordion-header" id={headingId}> 
                                <button className="accordion-button"
                                 type="button" data-bs-toggle="collapse" data-bs-target={`#${collapseId}`} 
                                 aria-expanded="true" aria-controls={collapseId}>
                                    <div className="d-flex flex-wrap justify-content-between align-items-center w-100">
                                        {/* To print question*/}
                                        <span className="text-start">{q.question}</span>
                                        {/*For badge printing active/inactive & diff level */}
                                        <div className="d-flex align-items-center gap-3">
                                            {/* <span className="d-flex align-items-center gap-1 text-black fw-medium">
                                                {q.isActive ? '✅' : '❌'} {q.isActive ? 'Active' : 'Inactive'}
                                            </span> */}
                                            {updatingId === q.id ? (
                                            <Spinner /> 
                                            ) :
                                            (<span title={q.isActive ? "Active" : "Inactive"}>
                                            <Switch
                                                checked={q.isActive === 1}
                                                onChange={() => handleToggle(q.id,q.isActive)}
                                                onColor="#00C851"
                                                offColor="#ff4444"
                                                uncheckedIcon={false}
                                                checkedIcon={false}
                                                />
                                            </span>
                                            )}
                                            {/* <span className="d-flex align-items-center gap-1 text-black fw-medium">
                                                📶 {q.diff_level}
                                            </span> */}
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
                                </button> </h2>
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
            </div>
        </>
)}

export default AccordionList;


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
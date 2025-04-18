import React from 'react';

  const AccordionList = ({ questions = [] ,categoryName}) => {
//   console.log("question d:-",quesitons);
    const accordionId = `accordion-${categoryName.replace(/\s+/g,"-").toLowerCase()}`;
    /*
        /.../ -> delimiter that defines regex pattern
        /s -> Matches any whitespace character (space, tab, newline)
        + -> Matches one or more of the preceding token (in this case, whitespace)
        g -> Global flag - replaces all matches ,not just the first
    */
    
    return (
        <>
            <div className="accordion" id={accordionId}>
                {questions.map((q,index)=>{
                    const collapseId =  `collapse-${q.category.id}-${index}`;
                    const headingId = `heading-${q.category.id}-${index}`;
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
                                            <span className="d-flex align-items-center gap-1 text-black fw-medium">
                                                {q.isActive ? '✅' : '❌'} {q.isActive ? 'Active' : 'Inactive'}
                                            </span>

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
                                    {[q.option_1,q.option_2,q.option_3,q.option_4].map((option,idx)=>{
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
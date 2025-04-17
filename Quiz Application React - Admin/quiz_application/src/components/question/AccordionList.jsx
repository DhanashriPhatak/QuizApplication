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

                                            <span className="d-flex align-items-center gap-1 text-black fw-medium">
                                                📶 {q.diff_level}
                                            </span>
                                        </div>
                                    </div>
                                </button> </h2>
                            {/* To show your options and answer*/}
                            <div id={collapseId} className="accordion-collapse collapse" data-bs-parent={`#${accordionId}`}>
                                <div className="accordion-body"> 
                                    1. {q.option_1} <br></br>
                                    2. {q.option_2} <br></br>
                                    3. {q.option_3} <br></br>
                                    4. {q.option_4} <br></br>
                                     <strong>Ans:-</strong> {q.ans}
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
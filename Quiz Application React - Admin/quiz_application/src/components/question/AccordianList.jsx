import React from 'react';

  const AccordianList = ({ data = [] }) => {
  console.log("question d:-",data);
  return (
    <div className="app-content"> {/*begin::Container*/}
        <div className="container-fluid"> {/*begin::Row*/}
            <div className="row g-4"> {/*begin::Col*/}
                <div className="col-md-12"> {/*begin::Accordion*/}
                    <div className="card card-primary card-outline mb-4"> {/*begin::Header*/}
                        <div className="card-header">
                            <div className="card-title">Accordion</div>
                        </div> {/*end::Header*/} {/*begin::Body*/}
                    
                    <div className="card-body">
                        <div className="accordion" id="accordionExample">
                            {data.map((q,index)=>{
                                const collapseId =  `collapse-${index}`;
                                const headingId = `heading-${index}`;
                                return (
                                    <div className="accordion-item" key={index}>
                                        <h2 className="accordion-header" id={headingId}> 
                                            <button className="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target={`#${collapseId}`} aria-expanded="true" aria-controls={collapseId}>
                                            {q.question}
                                            </button> </h2>
                                        <div id={collapseId} className="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                                            <div className="accordion-body"> <strong>This is the first item's accordion body.</strong> It is shown by default, until the collapse plugin
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
                                    </div>
                                )
                            })}
                            
                        </div>
                    </div> {/*end::Body*/}
          </div> {/*end::Accordion*/} {/*begin::Alert*/}
      </div> {/*end::Row*/}
    </div> {/*end::Container*/}
  </div> {/*end::App Content*/}
  </div>
)
}

export default AccordianList;


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
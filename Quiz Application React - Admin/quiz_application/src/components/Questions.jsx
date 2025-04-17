import React from 'react';
import Spinner from './common/Spinner';
import { useState,useEffect } from 'react';
import AccordianList from './question/AccordionList';
import { getQuestionsByCategory } from '../services/QuestionService';

const  Questions = ({categoryId,categoryName}) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [questions, setQuestions] = useState([]);
  const [showQuestion,setShowQuestion] = useState(false);
  const [fetched,setFetched] = useState(false);//prevent refetch
  
  console.log("category in quesiton:-"+categoryId);
  useEffect(()=>{
    if(showQuestion && !fetched)
    {
      setLoading(true);
      getQuestionsByCategory(categoryId).
      then(res=>{
        setQuestions(res.data);
        setFetched(true);
        setLoading(false);
      })
      .catch(error=>{
        // console.error(" Error fetching questions:", error);
        setError("Failed to load questions.");
        setLoading(false);
      });
    }
  },[showQuestion,fetched,categoryId]);

  return (
    <>
      <div className="app-content"> {/*begin::Container*/}
        <div className="container-fluid"> {/*begin::Row*/}
          <div className="row g-4"> {/*begin::Col*/}
            <div className="col-md-12"> {/*begin::Accordion*/}
              <div className="card card-primary card-outline mb-4"> {/*begin::Header*/}
                <div className="card-header" style={{cursor:'pointer'}} onClick={()=>setShowQuestion(!showQuestion)}>
                    <div className="card-title">{categoryName}</div>
                </div> {/*end::Header*/} {/*begin::Body*/}
                {showQuestion && (
                  <div className="card-body">
                    {loading?(<Spinner/>)
                      :error?(
                        <p className="text-danger">{error}</p>
                      ): questions.length === 0 ? (
                        <p>No questions available for this category.</p>
                      ) : (
                        <AccordianList 
                        questions={questions}
                        categoryName={categoryName}
                        />
                    )}
                  </div>
                )}
              </div> {/*end::Accordion*/} 
            </div> {/*end::Row*/}
          </div> {/*end::Container*/}
        </div> {/*end::App Content*/}
      </div>
    </>
  )
}

export default Questions;
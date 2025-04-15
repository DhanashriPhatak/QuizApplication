import React from 'react';
import Spinner from './common/Spinner';
import { useState,useEffect } from 'react';
import AccordianList from './question/AccordianList';
import { getAllQuestions } from '../services/QuestionService';

function Questions() {
  const [loading, setLoading] = useState(true);
  const [questions,setQuestions] = useState([]);
  const [error, setError] = useState('');

  useEffect(()=>{
    getAllQuestions().
    then(res=>{
      setQuestions(res.data);
      console.log("question print",res.data);
    })
    .catch(error=>{
      console.error(" Error fetching questions:", error);
        setError("Failed to load questions.");
    });
    
  },[]);

  return (
    <>
        <main className="app-main"> {/*begin::App Content Header*/}
            <div className="app-content-header"> {/*begin::Container*/}
                <div className="container-fluid"> {/*begin::Row*/}
                <div className="row">
                    <div className="col-sm-6">
                    <h3 className="mb-0">List of All Questions</h3>
                    </div>
                    <div className="col-sm-6 d-flex justify-content-end mb-3">
                      {/* <Link to="/questions/add" className="btn btn-primary"> */}
                        <i className="bi bi-plus-circle me-1"></i> Add New Question
                      {/* </Link> */}
                    </div>
                </div> {/*end::Row*/}
                </div> {/*end::Container*/}
            </div> {/*end::App Content Header*/} {/*begin::App Content*/}
            
                        {/* {loading?<Spinner/>:( */}
                          <AccordianList data={questions}/>
                        {/* )} */}
            
        </main> {/*end::App Main*/}

    </>
  )
}

export default Questions;
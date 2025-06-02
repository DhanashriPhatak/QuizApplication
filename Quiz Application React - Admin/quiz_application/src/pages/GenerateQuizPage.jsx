import React, { useEffect, useState } from 'react';
import QuizPreviewPanel from '../components/quiz/QuizPreviewPanel';
import GenerateQuizForm from '../components/quiz/GenerateQuizForm';
import { useParams, useSearchParams } from 'react-router-dom';
import { getQuizDetails } from '../services/QuizService';

const GenerateQuizPage = () => {
  const [quizId,setQuizId] = useState(null);
  const [error,setError] = useState();
  const {id} = useParams();
  const [searchParams] = useSearchParams();
  const mode = searchParams.get('mode');
  const [initialQuizData, setInitialQuizData] = useState(null);
  const isEditMode = !!id;

  useEffect(()=>{
    if(!id)
    {
      setQuizId(null);
      setInitialQuizData(null);
      return;
    }
    if(isEditMode && id)
    {
        setQuizId(id); 
        getQuizDetails(id)
        .then((res)=>{
          setInitialQuizData(res.data);
        })
        .catch((err)=>{
          console.error('Failed to fetch quiz details');
          setError("Failed to fetch quiz details");
        });
    }
  },[id]);
  return (
    <>
    <main className="app-main">
      <div className="app-content-header">
        <div className="container-fluid px-4 py-3">
          <div className="row align-items-start">
            <div className="col-lg-6 col-md-12 mb-4">
              <h3 className="mb-3 fw-bold">{isEditMode ?'Edit Quiz' :'Generate New Quiz'}</h3>
              <GenerateQuizForm editMode={isEditMode} initialData={initialQuizData} onPreviewUpdate={setQuizId}/>
            </div>
            {/* <div className="my-3 border-top border-2"></div> */}
            <div className="col-lg-6 col-md-12">
              <h3 className="mb-3 fw-bold">Quiz Preview</h3>
              <QuizPreviewPanel quizId={quizId} previewMode={!!quizId}/>
            </div>
          </div>
        </div>
      </div>
    </main>
    </>
  )
}

export default GenerateQuizPage;
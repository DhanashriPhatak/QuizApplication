import React, { useEffect,useState } from 'react';
import Questions from '../components/question/Questions';
import { getAllCategories } from '../services/QuestionService';
import Spinner from '../components/common/Spinner';

function QuestionsPage() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [categories,setCategories] = useState([]);
  // const [groupedQuestions, setGroupedQuestions] = useState({});
  
  useEffect(()=>{
    setLoading(true);
    getAllCategories()
    .then((res)=>{
      setCategories(res.data);
      setLoading(false);
    })
    .catch(error=>{
      setError("Failed to fetch categories");
      setLoading(false);
    })
  }, []);

  return (
    <>
      <main className="app-main"> {/*begin::App Content Header*/}
        <div className="app-content-header"> {/*begin::Container*/}
          <div className="container-fluid"> {/*begin::Row*/}
            <div className="row">
              <div className="col-sm-6">{/*  */}
                <h3 className="mb-0">List of All Questions</h3>{/* mb-0 */}
              </div>
              <div className="col-sm-6 d-flex justify-content-end mb-3">
                {/* <Link to="/questions/add" className="btn btn-primary"> */}
                  <button className="btn btn ">
                  <i className="bi bi-plus-circle me-1"></i> Add New Question
                  </button>
                {/* </Link> */}
              </div>
            </div> {/*end::Row*/}
          </div> {/*end::Container*/}
        </div> {/*end::App Content Header*/} 
        {loading?<Spinner/>
        :error?(
          <p className="text-danger">{error}</p>
        ):(
          categories.map((category,index)=>{
            return (
              <Questions
            key={index}
            categoryId={category.id}
            categoryName={category.category}
            ></Questions>
            );
          })
        )}
      </main>
    </>
  )
}

export default QuestionsPage;
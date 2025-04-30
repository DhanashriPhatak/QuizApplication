import React, { useRef, useState } from 'react'
import CategoriesStats from '../components/category/CategoriesStats'
import AddCategory from '../components/category/AddCategory'
import { CSSTransition } from 'react-transition-group';
import '../css/categoryFormTransition.css';

const CategoriesPage = () => {
  const [showForm,setShowForm] = useState(false);
  const [formMode,setformMode] = useState('add');
  const [editCategoryData,setEditCategoryData] = useState(null);
  const nodeRef = useRef(null);
  const [refreshCategories,setRefreshCategories] = useState(false);

  return (
    <>
      <main className="app-main"> {/*begin::App Content Header*/}
        <div className="app-content-header"> {/*begin::Container*/}
          <div className="container-fluid"> {/*begin::Row*/}
            <div className="row">
              <div className="col-sm-6">{/*  */}
                <h3 className="mb-0">
                  {showForm ? 
                  (formMode==='add'?"Add New Category" :"Update Category")
                  : "List of All Categories"
                  }
                  </h3>{/* mb-0 */}
              </div>
              <div className="col-sm-6 d-flex justify-content-end mb-3">
                {/* <Link to="/questions/add" className="btn btn-primary"> */}
                  <button className="btn btn-outline-dark " onClick={() => {
                    if(!showForm)
                    {
                      setformMode('add');
                      setEditCategoryData(null);
                    }
                    setShowForm(!showForm)
                  }}>
                  {showForm ? <i className="bi bi-arrow-left me-2"></i> : <i className="bi bi-plus-circle me-1"></i>}
                  {showForm ? "Back to Stats" : "Add New Category"}
                  </button>
                {/* </Link> */}
              </div>
            </div> {/*end::Row*/}
          </div> {/*end::Container*/}
        </div> {/*end::App Content Header*/}
        <CSSTransition
            in={showForm}
            timeout={300}
            classNames="fade-slide"
            unmountOnExit
            nodeRef={nodeRef}
          >
            <div ref={nodeRef}>
              <AddCategory 
              setShowForm={setShowForm}
              setRefreshCategories={setRefreshCategories}
              formMode={formMode}
              editCategoryData={editCategoryData}
              />
            </div>
          </CSSTransition>
          <div className="mt-4">  
            <CategoriesStats 
            refreshCategories={refreshCategories} 
            setRefreshCategories={setRefreshCategories}
            setformMode = {setformMode}
            setEditCategoryData ={setEditCategoryData}
            setShowForm ={setShowForm}
            />
          </div>
    </main>
    </>
  )
}

export default CategoriesPage
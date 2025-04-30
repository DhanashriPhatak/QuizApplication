import React, { useEffect, useState } from 'react'
import { addCategory,updateCategory } from '../../services/QuestionService';
import ShowToast from '../common/ShowToast';

const AddCategory = ({setShowForm,setRefreshCategories,formMode='add',editCategoryData=null}) => {
  const [categoryName,setCategoryName] = useState('');
  const [validated,setValidated] = useState(false);

  useEffect(()=>{
    if(formMode==='edit' && editCategoryData)
    {
      setCategoryName(editCategoryData.category);
    }
    else{
      setCategoryName('');
    }
  },[formMode,editCategoryData]);

  const handleSubmit = async(e) =>{
    e.preventDefault();
    setValidated(true);

    if(categoryName.trim() !== '')
    {
      const category = { category: categoryName.trim() };
      if(formMode==='edit' && editCategoryData)
      {
        updateCategory(editCategoryData.id,category)
        .then(respone=>{
          ShowToast({ type: 'success', title: 'Success', message: 'Category updated successfully!' });
          setShowForm(false);
          setRefreshCategories(prev=>!prev);
        })
        .catch(error=>{
          ShowToast({ type: 'error', title: 'Error', message: 'Failed to update category. Please try again.' });
          console.error("Error updating category:", error);
        });
      }
      else{
        //Add category Mode
        addCategory(category)
        .then(response=>{
          ShowToast({ type: 'success', title: 'Success', message: 'Category added successfully!' });
          setCategoryName('');
          setShowForm(false);  // Close the form
          setRefreshCategories(prev => !prev);
        })
        .catch(error=>{
          ShowToast({ type: 'error', title: 'Error', message: 'Failed to add category. Please try again.' });
          console.error("Error adding category:", error);
        })
        
      }
      setValidated(false);
    }
  }


  return (
    <>
      <div className="app-content">
        <div className="container-fluid">
          <div className="row g-4">
            <div className="col-md-6">
              <div className="card card-primary">
                {/* <div className="card-header">
                  <div className="card-title">Quiz</div>
                </div> */}
                <form  className={`needs-validation ${validated ? 'was-validated' : ''}`}
                noValidate
                onSubmit={handleSubmit}
                 >
                  <div className="card-body">
                    <div className="row g-3 align-items-center">
                      <label htmlFor="categoryName" className="col-sm-3 col-form-label">Category Name</label>
                      <div className="col-sm-9"> 
                          <input type="text" className="form-control"
                          id="categoryName" 
                          placeholder="e.g. Java"
                          value={categoryName}
                          onChange={(e)=> setCategoryName(e.target.value)}
                          required
                          />
                          <div className="invalid-feedback">Please enter a category name.</div>
                      </div>
                    </div>
                  </div>
                  <div className="card-footer"> 
                    <button className="btn btn-info" type="submit" >
                      {formMode==='edit'?(
                        <>
                        <i className="bi bi-pencil-square me-1"></i>Update Category
                      </>
                      ):(
                        <>
                        <i className="bi bi-plus-circle me-1"></i>Add Category
                        </>
                      )}
                  </button> </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default AddCategory
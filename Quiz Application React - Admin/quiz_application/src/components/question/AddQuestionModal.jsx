import React, { useEffect, useState } from 'react'
import '../../css/addQuestionModal.css';
import { getAllCategories } from '../../services/QuestionService';
import Spinner from '../common/Spinner';

const AddQuestionModal = ({show,onClose}) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [validated,setValidated] = useState(false);
  const [formData, setFormData] = useState({
    question: '',
    optionA: '',
    optionB: '',
    optionC: '',
    optionD: '',
    category: '',
    diffLevel: '',
    correctAnswer: ''
  });

    const [formErrors,setFormErrors] = useState({
      question:'',
      optionA:'',
      optionB:'',
      optionC:'',
      optionD:'',
      category:'',
      diffLevel:'',
      correctAnswer:''
    });
    /**Validated the form */
    function validateForm()
    {
      let valid = true;
      const errorCopy = {... formErrors};
      if(formData.question.trim().length >0)
      {
        errorCopy.question ="";
      }
      else{
        errorCopy.question = "Question cannot be empty.";
        valid = false;
      }
      if(formData.optionA.trim().length >0)
      {
        errorCopy.optionA = "";
      }
      else{
        errorCopy.optionA = "Option A is required.";
        valid=false;
      }
      if(formData.optionB.trim().length > 0)
      {
        errorCopy.optionB = "";
      }
      else{
        errorCopy.optionB = "Option B is required.";
        valid=false;
      }
      if(formData.optionC.trim().length > 0)
      {
        errorCopy.optionC = "";
      }
      else{
        errorCopy.optionC ="Option C is required.";
        valid = false;
      }
      if(errorCopy.optionD.trim().length > 0)
      {
        errorCopy.optionD = "";
      }
      else{
        errorCopy.optionD = "Option D is required.";
        valid = false;
      }
      setFormErrors(errorCopy);
      return valid;
    }
    
    /**Save the entered data  */
    const handleChange = (e)=>{
      const {name,value} = e.target;
      setFormData((prev)=>({
        ...prev,
        [name]:value,
      }));
    };
    /** Save question functionality */
    const handleSubmit = (e)=>{
      e.preventDefault();
      setValidated(true);

      if(validateForm()){

      }
    }

    const [categories,setCategories] = useState([]);
    useEffect(()=>{
      getAllCategories()
      .then((res)=>{
        setCategories(res.data);
        setLoading(false);
      })
      .catch((error)=>{
        setError("Failed to fetch categories");
        setLoading(false);
      })
    },[]);

  return (
    <>
      {show && (
        <div className="modal-backdrop-custom">
          <div className="modal show d-block" tabIndex="-1" role="dialog">
            <div className="modal-dialog modal-dialog-centered" role="document">
              <div className="modal-content rounded shadow">
                {/* Modal Header */}
                <div className="modal-header">
                  <h5 className="modal-title">Add New Question</h5>
                  <button type="button" className="btn-close" onClick={onClose}></button>
                </div>
                {/* Modal Body */}
                <form className={`needs-validation ${validated?'was-validated':''}`} noValidate 
                onSubmit={handleSubmit}>
                  <div className="modal-body">
                    <div className="row mb-3 align-items-center">
                      <label className="col-sm-3 col-form-label text-end" htmlFor="question">Question</label>
                      <div className="col-sm-9">
                      <textarea
                          className={`form-control ${validated && !formData.question.trim() ? 'is-invalid' : ''}`}
                          id="question"
                          name="question"
                          value={formData.question}
                          onChange={handleChange}
                          rows="3"
                          required
                        />
                        {formErrors.question && 
                          <div className="invalid-feedback">
                            {formErrors.question}
                          </div>
                        }
                      </div>
                    </div>
                    {/* Options */}
                    {['optionA', 'optionB', 'optionC', 'optionD'].map((key,index)=>(
                      <div className="row mb-3 align-items-center" key={key}>  
                        <label className="col-sm-3 col-form-label text-end"  htmlFor={key}>
                          Option {String.fromCharCode(65 + index)}
                        </label>
                        <div className="col-sm-9"> 
                          <input
                          type="text"
                          className={`form-control ${validated && !formData[key].trim() ? 'is-invalid' : ''}`}
                          id={key}
                          name={key}
                          value={formData[key]}
                          onChange={handleChange}
                          required
                          />
                          {formErrors[key] && 
                          <div className="invalid-feedback">
                            {formErrors[key]}
                          </div>
                        }
                        </div>
                      </div>
                    ))}

                    {/* Correct Answer */}
                    <div className="row mb-3 align-items-center">
                      <label className="col-sm-3 col-form-label text-end" htmlFor="correctAnswer">
                        Correct Answer
                      </label>
                      <div className="col-sm-9">
                        <select
                          id="correctAnswer"
                          name="correctAnswer"
                          className={`form-control ${validated && !formData.correctAnswer ? 'is-invalid' : ''}`}
                          value={formData.correctAnswer}
                          onChange={handleChange}
                          required
                        >
                          <option value="">Select correct option</option>
                          <option value="optionA">Option A</option>
                          <option value="optionB">Option B</option>
                          <option value="optionC">Option C</option>
                          <option value="optionD">Option D</option>
                        </select>
                        <div className="invalid-feedback">Please select the correct answer.</div>
                      </div>
                    </div>

                    {/* Category Dropdown */}
                    <div className="row mb-3 align-items-center">
                      <label className="col-sm-3 col-form-label text-end" htmlFor="category">Category</label>
                      <div className="col-sm-9">
                        <select
                        id="category"
                        name="category"
                        className={`form-control ${validated && !formData.category ? 'is-invalid':''}`}
                        value= {formData.category}
                        onChange={handleChange}
                        required
                        >
                          <option value="">Select Category</option>
                          {loading?<Spinner/>
                          :error?(
                            <p className="text-danger">{error}</p>
                          ):(
                            categories.map((category,index)=>{
                              return (
                                <option value={category.category} key={index}>{category.category}</option>
                              );
                            })
                          )}
                        </select>
                      </div>
                    </div>

                    {/* Difficulty Level Dropdown */}
                    <div className="row mb-3 align-items-center">
                      <label className="col-sm-3 col-form-label text-end" htmlFor="diffLevel">Difficulty</label>
                      <div className="col-sm-9">
                        <select
                          id="diffLevel"
                          name="diffLevel"
                          className={`form-control ${validated && !formData.diffLevel ? 'is-invalid' : ''}`}
                          value={formData.diffLevel}
                          onChange={handleChange}
                          required
                        >
                          <option value="">Select difficulty</option>
                          <option value="Easy">Easy</option>
                          <option value="Medium">Medium</option>
                          <option value="Hard">Hard</option>
                        </select>
                        <div className="invalid-feedback">Please select difficulty level.</div>
                      </div>
                    </div>
                  </div>
                </form>
                {/* Modal Footer */}
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={onClose}>Cancel</button>
                  <button className="btn btn-primary">Save Question</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  )
}

export default AddQuestionModal
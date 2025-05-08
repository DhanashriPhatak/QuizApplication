import React from 'react';
import { useState ,useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getAllCategories } from '../../services/QuestionService';
import Spinner from '../common/Spinner';
import ShowToast from '../common/ShowToast';

const GenerateQuizForm = ({onPreviewUpdate}) => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [errors,setErrors] = useState({});

    const [formData,setFormData] = useState({
        quizName:'',
        totalQuestions: '',
        selectedCategories:[],
        categoryConfig: {}, //{[categoeyod]:{difficulty:'',count:''}}
    });

    
    /**START :- populate categories */
    const [categories,setCategories] = useState([]);
    useEffect(()=>{
        setLoading(true);
        setError('');
        getAllCategories()
        .then((res)=>{
            setCategories(res.data || []);
            console.log(categories);
            setError('');
        })
        .catch((error)=>{
            setError("Failed to fetch categories");
        })
        .finally(()=>{
            setLoading(false);
        })
    },[]);
    /**END :- populate categories */
    /**START - Save the entered data  */
    const handleChange = (e)=>{
        const {name,value} = e.target;
        setFormData((prev)=>({
          ...prev,
          [name]:value,
        }));
        setErrors((prev)=>({
            ...prev,
            [name]:''
        }));
      };
    /**END - Save the entered data  */
    /**START -save Category changed  */
    const handleCategoryChange = (e)=>{
        const options = e.target.options;
        const selected = [];

        for(let i=0;i<options.length;i++)
        {
            if(options[i].selected)
            {
                selected.push(parseInt(options[i].value));
            }
        }

        //reset removed configs
        const updatedConfig = {};
        selected.forEach(id=>{
            updatedConfig[id] = formData.categoryConfig[id] || {difficulty:'',count:''};
        });

        setFormData(prev=>({
            ...prev,
            selectedCategories:selected,
            categoryConfig:updatedConfig
        }));
        setErrors((prev) => ({ ...prev, selectedCategories: '' }));
    };
    /**END - Save Category changed */
    /**START - Save Category config changed */
    const handleCategoryConfigChanged = (catId,field,value)=>{
        setFormData(prev=>({
            ...prev,
            categoryConfig:{
                ...prev.categoryConfig,
                [catId]:{
                    ...prev.categoryConfig[catId],
                    [field]:value
                }
            }
        }));
    };
    /**END - Save Category config changed */
    
    /**START - Validate form */
    const validateForm = ()=>{
        const errs = {};

        if(!formData.quizName.trim())
        {
            errs.quizName = 'Quiz name is required';
        }
        if(formData.totalQuestions || isNaN(formData.totalQuestions))
        {
            errs.totalQuestions = 'Enter a valid total question count';
        }
        if (formData.selectedCategories.length === 0) errs.selectedCategories = 'Select at least one category';
        let total = 0;
        formData.selectedCategories.forEach((id)=>{
            const config = formData.categoryConfig[id];
            if(!config.difficulty)
            {
                errs[`difficulty_${id}`] = 'Select difficulty';
            }
            if(!config.count)
            {
                errs[`count_${id}`] = 'Enter valid count';
            }else {
                total += parseInt(config.count);
            }
        });
        if(total != parseInt(formData.totalQuestions))
        {
            errs.totalMismatch = 'Sum of category-wise questions must equal total quiz questions';
        }
        setErrors(errs);
        return Object.keys(errs).length ===0;
    };
    /**END - Validate form */
    /**START - Submit form */
    const handleSubmit = (e)=>{
        e.preventDefault();
        if(!validateForm()) return;
        if(onPreviewUpdate)
        {
            onPreviewUpdate(formData);
        }

        ShowToast({type:'success',title:'Preview Ready',message:'Check the right panel'});
    }
    /**END - Submit form */
    
    if (loading) return <Spinner />;
    if (error) return <div className="alert alert-danger">{error}</div>;
    return (
        <>
        <div className="card card-info card-outline">
            <div className="card-header">
                <h3 className="card-title">Quiz</h3>
            </div>
            <form className="needs-validation" onSubmit={handleSubmit} noValidate>
                <div className="card-body">
                    <div className="row g-3">
                    <div className="col-md-12">
                        <label htmlFor="quizName" className="form-label">Quiz Name</label>
                        <input
                        type="text"
                        className={`form-control ${errors.quizName ? 'is-invalid' : ''}`}
                        id="quizName"
                        name="quizName"
                        value={formData.quizName}
                        onChange={handleChange}
                        required
                        />
                        {errors.quizName && <div className="invalid-feedback">{errors.quizName}</div>}
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="totalQuestions" className="form-label">Total Questions</label>
                        <input
                        type="number"
                        className={`form-control ${errors.totalQuestions ? 'is-invalid' : ''}`}
                        id="totalQuestions"
                        name="totalQuestions"
                        value={formData.totalQuestions}
                        onChange={handleChange}
                        required
                        />
                        {errors.totalQuestions && <div className="invalid-feedback">{errors.totalQuestions}</div>}
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="selectCategories" className="form-label">Select Categories</label>
                        <select
                        multiple
                        className={`form-control ${errors.selectedCategories ? 'is-invalid' : ''}`}
                        id="selectCategories"
                        value={formData.selectedCategories}
                        onChange={handleCategoryChange}
                        >
                        {categories.map((cat) => (
                            <option key={cat.categoryId} value={cat.id}>{cat.category}</option>
                        ))}
                        </select>
                        {errors.selectedCategories && <div className="invalid-feedback">{errors.selectedCategories}</div>}
                    </div>

                    {formData.selectedCategories.map((catId) => {
                        const category = categories.find((c) => c.categoryId === parseInt(catId));
                        const config = formData.categoryConfig[catId] || {};
                        return (
                        <div key={catId} className="border rounded p-3 mb-2">
                            <strong>{category?.categoryName}</strong>
                            <div className="row mt-2">
                            <div className="col-md-6">
                                <label className="form-label">Difficulty</label>
                                <select
                                className={`form-control ${errors[`difficulty_${catId}`] ? 'is-invalid' : ''}`}
                                value={config.difficulty}
                                onChange={(e) => handleCategoryConfigChanged(catId, 'difficulty', e.target.value)}
                                >
                                <option value="">Choose...</option>
                                <option value="Easy">Easy</option>
                                <option value="Medium">Medium</option>
                                <option value="Hard">Hard</option>
                                </select>
                                {errors[`difficulty_${catId}`] && (
                                <div className="invalid-feedback">{errors[`difficulty_${catId}`]}</div>
                                )}
                            </div>
                            <div className="col-md-6">
                                <label className="form-label">Number of Questions</label>
                                <input
                                type="number"
                                className={`form-control ${errors[`count_${catId}`] ? 'is-invalid' : ''}`}
                                value={config.count}
                                onChange={(e) => handleCategoryConfigChanged(catId, 'count', e.target.value)}
                                />
                                {errors[`count_${catId}`] && (
                                <div className="invalid-feedback">{errors[`count_${catId}`]}</div>
                                )}
                            </div>
                            </div>
                        </div>
                        );
                    })}
                    {errors.totalMismatch && <div className="text-danger fw-semibold">{errors.totalMismatch}</div>}
                    </div>
                </div>
                <div className="card-footer">
                    <button className="btn btn-info" type="submit">Generate Quiz</button>
                </div>
            </form>
            </div>
        </>
    )
};

export default GenerateQuizForm
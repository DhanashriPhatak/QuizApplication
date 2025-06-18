import React from 'react';
import { useState ,useEffect} from 'react';
import { getActiveQuestionCountByCategory } from '../../services/QuestionService';
import { generateQuiz,generateQuizManual,updateQuiz,updateQuizManual} from '../../services/QuizService';
import Spinner from '../common/Spinner';
import ShowToast from '../common/ShowToast';
import { useNavigate } from 'react-router-dom';

const GenerateQuizForm = ({editMode=false, initialData = null, onPreviewUpdate}) => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [errors,setErrors] = useState({});
    const navigate = useNavigate();

    const [formData,setFormData] = useState({
        quizName:'',
        totalQuestions: '',
        selectedCategories:[],
        categoryConfig: {}, //{[categoeyod]:{difficulty:'',count:''}}
        mode:'auto' // or manual
    });

    /**START :- populate categories */
    const [categories,setCategories] = useState([]);
    useEffect(()=>{
        setLoading(true);
        setError('');
        getActiveQuestionCountByCategory()
        .then((res)=>{
            console.log(res.data);
            const mapped = (res.data || []).map(c => ({
                id: c.categoryId,
                category: c.category,
                availableCount: c.total,
                difficultyCounts: {
                    Random: c.total,
                    Easy: c.easyCount,
                    Medium: c.mediumCount,
                    Hard: c.hardCount
                }
              }));
            setCategories(mapped);
            setError('');

            if(editMode && initialData)
            {
                console.log("initial Data:-",initialData);
                const nameToMap = {};
                mapped.forEach(c=>{
                    nameToMap[c.category.trim().toLowerCase()] = c.id;
                })

                const updatedCategoryConfig = {};
                const selectedCategories = [];
                let total = 0;
                const groupedCategoryData = {};
                (initialData.categoryDifficultyPairList || []).forEach(pair=>{
                    const key = pair.categoryName.trim().toLowerCase();
                    const categoryId = nameToMap[key];
                    if (categoryId === undefined) {
                        console.warn(`Category "${pair.categoryName}" not found in active categories`);
                        return; // Don't break app, just warn
                    }
                    
                    if(!groupedCategoryData[categoryId]){
                        groupedCategoryData[categoryId] = {
                            count:0,
                            difficulties:new Set()
                        };
                    }

                    groupedCategoryData[categoryId].count +=parseInt(pair.count,10);
                    groupedCategoryData[categoryId].difficulties.add(pair.diffLevel);

                    // console.log("Count for category id:-",categoryId," count:-",pair.count);
                    total+=pair.count;
                    
                });

                for(const[catId,data] of Object.entries(groupedCategoryData))
                {
                    const difficulty = data.difficulties.size===1 ? [...data.difficulties][0] : 'Random';

                    updatedCategoryConfig[catId] = {
                        difficulty,
                        count :data.count
                    };
                    selectedCategories.push(parseInt(catId));
                }

                setFormData({
                    quizName: initialData.quizTitle,
                    totalQuestions: total,
                    selectedCategories: selectedCategories,
                    categoryConfig: updatedCategoryConfig,
                    mode: initialData.mode || 'manual'
                });
                
            }
        })
        .catch((error)=>{
            setError("Failed to fetch categories");
        })
        .finally(()=>{
            setLoading(false);
        })
    },[editMode,initialData]);
     useEffect(() => {
        if (!editMode && initialData === null) {
            setFormData({
                quizName: '',
                totalQuestions: '',
                selectedCategories: [],
                categoryConfig: {},
                mode: 'auto'
            });
            setErrors({});
        }
    }, [editMode, initialData]);
    /**END :- populate categories */
    /**START - Save the entered data  */
    const handleChange = (e)=>{
        setError('');
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
        console.log("selected caegory:-",formData);
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
        let totalAvailable = 0;
        if(!formData.quizName.trim())
        {
            errs.quizName = 'Quiz name is required';
        }
        if(!formData.totalQuestions || isNaN(formData.totalQuestions))
        {
            errs.totalQuestions = 'Enter a valid total question count';
        }
        formData.selectedCategories.forEach((id) => {
            const available = categories.find(c => c.id === id)?.availableCount || 0;
            totalAvailable += available;
          });
        
        if (parseInt(formData.totalQuestions) > totalAvailable) {
        errs.totalQuestions = 'Total questions exceed available question pool';
        }
        if (formData.mode === 'manual') 
        {
            if (formData.selectedCategories.length === 0) errs.selectedCategories = 'Select at least one category';
            let total = 0;
            formData.selectedCategories.forEach((id)=>{
                const config = formData.categoryConfig[id];
                const available = categories.find((c) => c.id === id)?.availableCount || 0;
                if(!config.difficulty)
                {
                    errs[`difficulty_${id}`] = 'Select difficulty';
                }
                if(!config.count)
                {
                    errs[`count_${id}`] = 'Enter valid count';
                }else {
                    const requestedCount = parseInt(config.count);
                    const categoryObj = categories.find((c) => c.id === id);
                    const availableByDifficulty = categoryObj?.difficultyCounts?.[config.difficulty] || 0;
                    if(requestedCount > available)
                    {
                        errs[`count_${id}`] = `Only ${available} total questions available in this category`;
                    }
                    else if(requestedCount > availableByDifficulty)
                    {
                        errs[`count_${id}`] =  `Only ${availableByDifficulty} '${config.difficulty}' questions available in this category`;
                    }
                    else{
                        total+=requestedCount;
                    }
                }
            });

            if(total != parseInt(formData.totalQuestions))
            {
                errs.totalMismatch = 'Sum of category-wise questions must equal total quiz questions';
            }
        }

        setErrors(errs);
        return Object.keys(errs).length ===0;
    };
    /**END - Validate form */
    /**START - Submit form */
    const handleSubmit = (e)=>{
        e.preventDefault();
        if(!validateForm()) return;
        setLoading(true);
        setError('');
        // console.log("Inside submit:-",formData.mode);
        let response;
        let payload = "";
        
        if(formData.mode === 'manual')
        {
            payload = {
                quizTitle:formData.quizName,
                mode:formData.mode,
                configList: formData.selectedCategories.map((catId)=>({
                    categoryId:catId,
                    diffLevel:formData.categoryConfig[catId].difficulty,
                    numberOfQuestions: parseInt(formData.categoryConfig[catId].count)
                    }))
                };
            console.log("payload:-",payload);
            if(editMode)
            {
                payload.quizId = initialData.quizId;
                response = updateQuizManual(payload);
            }
            else{
                response = generateQuizManual(payload);
            }
        }
        else{
            payload = {
            categoryId: [...formData.selectedCategories], 
            quizTitle: formData.quizName,
            mode:formData.mode,
            numberOfQuestions: parseInt(formData.totalQuestions),
            };
            console.log("payload:-",payload);
            if(editMode)
            {
                payload.quizId = initialData.quizId;
                response = updateQuiz(payload);
            }
            else{
                response = generateQuiz(payload);
            }
            
        }
        response
        .then((res)=>{
            if(res.status===200)
            {
                ShowToast({type: 'success',title: 'Quiz Created',message: 'Your quiz is ready. Check the right panel.'});
                if(onPreviewUpdate)
                {
                    console.log("quiz id:-",res.data);
                    onPreviewUpdate(res.data);
                }
                handleReset();
            }
            else {
                ShowToast({type: 'error',title: 'Error',message: 'Quiz generation failed. Please try again.'});
            }
        })
        .catch((error)=>{
            console.log(error);
            setError('Failed to generate a quiz');
            ShowToast({type: 'error',title: 'Error',message: error?.response?.data || 'Unexpected error occurred'});
        })
        .finally(()=>{
            setLoading(false);
        });
    }
    /**END - Submit form */
    /**START - reset form */
    const handleReset = ()=>{
        setFormData({
        quizName:'',
        totalQuestions: '',
        selectedCategories:[],
        categoryConfig: {}, 
        mode:'auto' 
        });
        setErrors({});
        ShowToast({ type: 'info', title: 'Reset', message: 'Form has been cleared' });
    }
    /**End - reset form */
    if (loading) return <Spinner />;
    
    return (
        <>
        {error &&  <div className="alert alert-danger">{error}</div>}
        <div className="card card-info card-outline">
            {editMode && (
                <div className="alert alert-warning mt-2">
                    You are currently editing this quiz. Changes will overwrite the original.
                </div>
            )}
            {/* <div className="card-header">
                <h3 className="card-title">{editMode ?'Edit Quiz' :'Generate New Quiz'}</h3>
            </div> */}
            <div className="needs-validation" >
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

                    <div className="col-md-12">
                    <label className="form-label">Select Quiz Mode</label>
                    <div>
                        <div className="form-check form-check-inline">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="mode"
                            id="manualMode"
                            value="manual"
                            checked={formData.mode === 'manual'}
                            onChange={handleChange}
                        />
                        <label className="form-check-label" htmlFor="manualMode">
                            Manually set difficulty and number of questions
                        </label>
                        </div>
                        <div className="form-check form-check-inline">
                        <input
                            className="form-check-input"
                            type="radio"
                            name="mode"
                            id="autoMode"
                            value="auto"
                            checked={formData.mode === 'auto'}
                            onChange={handleChange}
                        />
                        <label className="form-check-label" htmlFor="autoMode">
                            Auto-generate questions randomly
                        </label>
                        </div>
                    </div>
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
                        <label htmlFor="selectCategories" className="form-label">
                            Select Categories<small className="text-muted">(Available questions)</small>
                        </label>
                        <select
                        multiple
                        className={`form-control ${errors.selectedCategories ? 'is-invalid' : ''}`}
                        id="selectCategories"
                        value={formData.selectedCategories}
                        onChange={handleCategoryChange}
                        >
                        {categories.map((cat) => (
                            <option key={cat.categoryId} value={cat.id}>{cat.category} ({cat.availableCount})</option>
                        ))}
                        </select>
                        {errors.selectedCategories && <div className="invalid-feedback">{errors.selectedCategories}</div>}
                    </div>

                    {formData.mode === 'manual' && formData.selectedCategories.map((catId) => {
                        const category = categories.find((c) => c.id === parseInt(catId));
                        const config = formData.categoryConfig[catId] || {};
                        return (
                        <div key={catId} className="border rounded p-3 mb-2">
                            <strong>
                                {category?.category}
                                <span className="text-muted">
                                    {' '}
                                    ( Easy: {category?.difficultyCounts?.Easy || 0 } | {' '}
                                    Medium: {category?.difficultyCounts?.Medium || 0} | {' '}
                                    Hard: {category?.difficultyCounts?.Hard || 0 }
                                    )</span>
                            </strong>
                            <div className="row mt-2">
                            <div className="col-md-6">
                                <label className="form-label">Difficulty</label>
                                <select
                                className={`form-control ${errors[`difficulty_${catId}`] ? 'is-invalid' : ''}`}
                                value={config.difficulty}
                                onChange={(e) => handleCategoryConfigChanged(catId, 'difficulty', e.target.value)}
                                >
                                <option value="">Choose...</option>
                                <option value="Random">Random</option>
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
                <div className="card-footer d-flex justify-content-end gap-2">
                    <button className="btn btn-secondary" type="button" onClick={handleReset}>Reset Form</button>
                    <button className="btn btn-info" type="button" onClick={handleSubmit}>
                        {editMode ? 'Save Changes & Regenerate Preview' :'Generate Quiz Preview'}
                    </button>
                    {editMode && (
                        <button className="btn btn-outline-secondary" onClick={() => navigate('/quiz')}>
                    Cancel Editing
                    </button>
                   )}
                </div>
                </div>
            {/* </form> */}
            </div>
        </>
    )
};

export default GenerateQuizForm
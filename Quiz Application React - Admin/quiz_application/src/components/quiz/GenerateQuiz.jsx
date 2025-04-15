import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const GenerateQuiz = () => {
    console.log("✅ GenerateQuiz component loaded");

    const [quizName,setQuizName] = useState('');
    const [topic, setTopic] = useState("Choose...");
    const [diffLevel, setDiffLevel] = useState("Choose...");
    const [noOfQuestions, setNoOfQuestions] = useState("Choose...");

    const navigate = useNavigate();

    const [errors,setErrors] = useState({
        quizNameError:'',
        topicError:'',
        diffLevelError:'',
        noOfQuestionError:''
    });

    

    function validateForm()
    {
        let valid = true;
        
        const errorCopy = {... errors};

        //Check if quiz name is empty or not and set the error message accordingly
        if(quizName.trim().length > 0 )
        {
            errorCopy.quizNameError="";
        }
        else{
            errorCopy.quizNameError="Please Give a Quiz Name.";
            valid = false;
        }

        //Check if quiz topic is empty or not and set the error message accordingly
        if(topic.trim()!= "Choose...")
        {
            errorCopy.topicError = "";
        }
        else{
            errorCopy.topicError = "Please select a valid Topic.";
            valid = false;
        }

        //Check if difficulty level is empty or not and set the error message accordingly
        if(diffLevel.trim()!= "Choose...")
        {
            errorCopy.diffLevelError = "";
        }
        else{
            errorCopy.diffLevelError = "Please select a valid Difficulty Level.";
            valid = false;
        }

        //Check if No of questions is empty or not and set the error message accordingly
        if(noOfQuestions.trim()!="Choose...")
        {
            errorCopy.noOfQuestionError = "";
        }
        else{
            errorCopy.noOfQuestionError = "Please select a Number of Questions in a Quiz.";
            valid = false;
        }

        setErrors(errorCopy);
        return valid;

    }

    const submitForm = (e)=>{
        e.preventDefault();
        if(validateForm())
        {
            console.log("success");

            const newQuiz = {
                quizName,
                topic,
                diffLevel,
                noOfQuestions
            };
            createQuiz(newQuiz);

            return navigate('/jobs');
        }
        else{
            console.log("error");
            toast.error('Something went wrong');
        }
    }

    const createQuiz = async (newQuiz)=>{
        const res = await fetch('/api/jobs',{
          method :'POST',
          headers : {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(newQuiz),
        });
        console.log(res);
      };

    return (
        <>
            <main className="app-main">
                <div className="app-content-header">
                    <div className="container-fluid">
                        <div className="row">
                            <div className="col-sm-6">
                                <h3 className="mb-0">Generate Quiz</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="app-content">
                    <div className="container-fluid">
                        <div className="row g-4">
                            <div className="col-md-6">
                                <div className="card card-info card-outline mb-4">
                                    <div className="card-header">
                                        <div className="card-title">Quiz</div>
                                    </div>
                                    <form className="needs-validation" onSubmit={submitForm}>
                                        <div className="card-body">
                                            <div className="row g-3">
                                                <div className="col-md-12"> 
                                                    <label htmlFor="quizName" className="form-label">Quiz Name</label>
                                                    <div className="input-group has-validation">
                                                        <input type="text" 
                                                        className={`form-control ${ errors.quizNameError ? 'is-invalid':''}`} 
                                                        id="quizName"  
                                                        value={quizName}
                                                        onChange={(e)=> setQuizName(e.target.value)}/>
                                                        {errors.quizNameError && <div className="invalid-feedback">
                                                            {errors.quizNameError}
                                                        </div>
                                                        }
                                                    </div>
                                                </div>
                                                <div className="col-md-6"> 
                                                    <label htmlFor="selectTopic" className="form-label">Topic</label>
                                                    <div className="input-group has-validation">
                                                        <select 
                                                        className={`form-control ${ errors.topicError ? 'is-invalid':''}`} 
                                                         id="selectTopic" required
                                                        value={topic}
                                                        onChange={(e)=> setTopic(e.target.value)}
                                                        >
                                                            <option disabled value="Choose..." id="0">Choose...</option>
                                                            <option id="1" value="Java">Java</option>
                                                            <option id="2" value="Python">Python</option>
                                                        </select>
                                                        {errors.topicError && <div className="invalid-feedback">
                                                            {errors.topicError}
                                                        </div>
                                                        }
                                                    </div>
                                                </div> 
                                                <div className="col-md-6"> 
                                                <label htmlFor="selectDifficultyLevel" className="form-label">Difficulty Level</label>
                                                    <select 
                                                    className={`form-control ${ errors.diffLevelError ? 'is-invalid':''}`}  
                                                    id="selectDifficultyLevel" required 
                                                    value={diffLevel}
                                                    onChange={(e)=> setDiffLevel(e.target.value)}
                                                    >
                                                        <option disabled value="Choose..." id="0">Choose...</option>
                                                        <option id="1" value="Easy">Easy</option>
                                                        <option id="2" value="Medium">Medium</option>
                                                        <option id="3" value="Hard">Hard</option>
                                                        <option id="4" value="Mix">Mix</option>
                                                    </select>
                                                    {errors.diffLevelError && <div className="invalid-feedback">
                                                            {errors.diffLevelError}
                                                        </div>
                                                        }
                                                </div> 
                                                <div className="col-md-6"> 
                                                    <label htmlFor="selectNumberOfQuestions" className="form-label">Number of Questions for a quiz</label>
                                                    <select 
                                                    className={`form-control ${ errors.noOfQuestionError ? 'is-invalid':''}`}  
                                                    id="selectNumberOfQuestions" required
                                                    value={noOfQuestions}
                                                    onChange={(e)=> setNoOfQuestions(e.target.value)}
                                                    >
                                                        <option id="0" disabled value="Choose...">Choose...</option>
                                                        <option id="1" value="1">1</option>
                                                        <option id="2" value="10">10</option>
                                                    </select>
                                                    {errors.noOfQuestionError && <div className="invalid-feedback">
                                                            {errors.noOfQuestionError}
                                                        </div>
                                                    }
                                                </div>
                                                <div className="col-md-6"> 
                                                    <label htmlFor="txtNumberOfQuestiosAvailable" className="form-label">Number of Questions Available</label>
                                                    <input type="text" className="form-control" id="txtNumberOfQuestiosAvailable" disabled />
                                                </div>
                                            </div>
                                        </div>
                                        <div className="card-footer"> <button className="btn btn-info" type="submit" >Generate Quiz</button> </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>

        </>

    )
};

export default GenerateQuiz
import React, { useEffect, useState } from 'react'
import QuizList from './QuizList';
import { getActiveInactiveCount } from '../../services/QuizService';
import Spinner from '../common/Spinner';

const Quiz = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [activeTab,setActiveTab] = useState("active");
    const [quizCount,setQuizCount] = useState([]);
    const [reloadTrigger,setReloadTrigger]  =useState(0);

    useEffect(()=>{
        setLoading(true);
        setError('');
        getActiveInactiveCount()
        .then((res)=>{
            // console.log(res.data);
            setQuizCount(res.data);
            setLoading(false);
            setError('');
        })
        .catch((err)=>{
            setError("Failed to load count for Active/Inactive Quiz");
            setLoading(false);
        })
    },[]);

    const handleReloadTrigger = ()=>{
        setReloadTrigger(prev=>prev+1);
    }

    if (loading) return <Spinner />;

  return (
    <>
        {error &&  <div className="alert alert-danger">{error}</div>}
        <div className="card card-primary card-outline">
            <div className="card-header p-2">
                <ul className="nav nav-pills">
                <li className="nav-item">
                    <a
                    className={`nav-link ${activeTab === 'active' ? 'bg-success #20c997 text-white':'text-dark' }`}
                    href="#active"
                    onClick={(e) => {
                        e.preventDefault();
                        setActiveTab('active');
                    }}
                    >
                    Active Quizzes ({quizCount.active})
                    </a>
                </li>
                <li className="nav-item">
                    <a
                    className={`nav-link ${activeTab === 'inactive' ? 'bg-secondary #dee2e6 text-white':'trxt-dark'}`}
                    href="#inactive"
                    onClick={(e) => {
                        e.preventDefault();
                        setActiveTab('inactive');
                    }}
                    >
                    Inactive Quizzes ({quizCount.inactive})
                    </a>
                </li>
                </ul>
            </div>
            <div className="card-body">
                <div className="tab-content">
                <div className={`tab-pane ${activeTab === 'active' ? 'active' : ''}`} id="active">
                    <QuizList status="active" reloadTrigger={reloadTrigger} onReload={handleReloadTrigger}/>
                </div>
                <div className={`tab-pane ${activeTab === 'inactive' ? 'active' : ''}`} id="inactive">
                    <QuizList status="inactive" reloadTrigger={reloadTrigger} onReload={handleReloadTrigger}/>
                </div>
                </div>
            </div>
        </div>
    </>
  )
}

export default Quiz;
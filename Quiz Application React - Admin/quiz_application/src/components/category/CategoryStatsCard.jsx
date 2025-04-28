import React, { useState } from 'react';
import { deleteCategory } from '../../services/QuestionService';
import ShowToast from '../common/ShowToast';

const CategoryStatsCard = ({data,setRefreshCategories}) => {
    const [showDetails,setShowDetails] = useState(false);
    const[showConfirmModal,setShowConfirmModal] = useState(false);
    const {
        categoryId,
        categoryName,
        totalQuestions,
        easyCount,
        mediumCount,
        hardCount,
        activeCount,
        inactiveCount
      } = data;

    const handleEdit = ()=>{
        
    }

    const handleDelete = () =>{
        setShowConfirmModal(true);
    }

    const handleDeleteConfirmed = async()=>{
        deleteCategory(categoryId)
        .then((res)=>{
            console.log(res.data);
            setShowConfirmModal(false);
            setRefreshCategories(prev => !prev);
            ShowToast({ type: 'success', title: 'Success', message: 'Category Deleted successfully!' });
        })
        .catch(error=>{
            console.log("error:-"+error);
            ShowToast({type:'error',title:'Error',message:'Failed to delete category. Please try again.'});
        });
    };

    return (
        <>
            <div className="col-12 col-sm-6 col-lg-3 mb-4">
                <div className="card border-primary shadow-sm">
                    <div className="card-body p-3">
                        <div className="d-flex justify-content-between align-items-start">
                        <div>
                            <div className="d-flex align-items-center mb-1">
                                <i className="bi bi-tags-fill text-primary me-2 fs-5"></i>
                                <h5 className="card-title mb-0">{categoryName}</h5>
                            </div>
                            <p className="card-text fw-semibold mb-0">Total Questions: {totalQuestions}</p>
                        </div>
                        <div className="d-flex align-items-center gap-2">
                            <button
                            className="btn btn-sm btn-outline-primary p-1 d-flex align-items-center justify-content-center"
                            style={{ width: '30px', height: '30px' }}
                            onClick={()=>setShowDetails(!showDetails)}
                            title="Toggle Stats"
                            >
                                <i className={`bi ${showDetails? 'bi-chevron-up':'bi-chevron-down'}`}></i>
                            </button>

                            <div className="dropdown">
                                <button
                                className="btn btn-sm btn-outline-secondary p-1 d-flex align-items-center justify-content-center"
                                style={{ width: '30px', height: '30px' }}
                                type="button"
                                id={`dropdownMenuButton-${categoryId}`}
                                data-bs-toggle="dropdown"
                                aria-expanded="false"
                                title="More Options"
                                >
                                    <i className="bi bi-three-dots-vertical"></i>
                                </button>
                                <ul
                                className="dropdown-menu dropdown-menu-end"
                                aria-labelledby={`dropdownMenuButton-${categoryId}`}
                                >
                                    <li>
                                        <button className="dropdown-item" onClick={handleEdit}>
                                            <i className="bi bi-pencil-square me-2"></i>Edit
                                        </button>
                                    </li>
                                    <li>
                                        <button className="dropdown-item text-danger" onClick={handleDelete}>
                                        <i className="bi bi-trash3-fill me-2"></i>Delete
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div> 
                    </div>
                    {showDetails && (
                          <div className="card-footer bg-white pt-3 border-top">
                            <div className="d-flex flex-wrap gap-2 mb-2">
                                <span className="badge bg-success">Easy: {easyCount}</span>
                                <span className="badge bg-warning text-dark">Medium: {mediumCount}</span>
                                <span className="badge bg-danger">Hard: {hardCount}</span>
                            </div>
                            <div className="d-flex flex-wrap gap-2">
                                <span className="badge bg-info text-dark">Active: {activeCount}</span>
                                <span className="badge bg-secondary">Inactive: {inactiveCount}</span>
                          </div>
                        </div>
                    )}
                </div>
            </div>
            {showConfirmModal && (
            <div className="modal fade show d-block" tabIndex="-1" role="dialog" style={{backgroundColor:'rgba(0,0,0,0.5)'}}>
                <div className="modal-dialog" role="document">
                <div className="modal-content">
                    
                    <div className="modal-header bg-danger text-white">
                    <h5 className="modal-title">Confirm Delete</h5>
                    <button type="button" className="btn-close" onClick={() => setShowConfirmModal(false)}></button>
                    </div>

                    <div className="modal-body">
                    Deleting this category will also delete <strong>{totalQuestions}</strong> questions.<br/>
                    Are you sure you want to continue?
                    </div>

                    <div className="modal-footer">
                    <button type="button" className="btn btn-secondary" onClick={() => setShowConfirmModal(false)}>Cancel</button>
                    <button type="button" className="btn btn-danger" onClick={handleDeleteConfirmed}>Yes, Delete</button>
                    </div>

                </div>
                </div>
            </div>
            )}
        </>
    )
};

export default CategoryStatsCard

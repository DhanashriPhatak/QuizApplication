import React from 'react';
import Spinner from './Spinner';

const ConfirmModal = ({
    title="Are you sure?",
    message = "Please confirm this action",
    show,
    onClose,
    onConfirm,
    confirmText = "Yes, confirm",
    confirmBtnClass = "btn-danger",
    loading=false
}) => {
    if(!show)return null;

  return (
    <>
    return (
    <div className="modal fade show d-block" tabIndex="-1" role="dialog" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
      <div className="modal-dialog" role="document">
        <div className="modal-content">
          <div className={`modal-header text-white ${confirmBtnClass === 'btn-danger' ? 'bg-danger' : 'bg-primary'}`}>
            <h5 className="modal-title">{title}</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">{message}</div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            <button type="button" className={`btn ${confirmBtnClass}`} onClick={onConfirm} disabled={loading}>
              {loading ? <Spinner /> : confirmText}
            </button>
          </div>
        </div>
      </div>
    </div>
    </>
  )
}

export default ConfirmModal
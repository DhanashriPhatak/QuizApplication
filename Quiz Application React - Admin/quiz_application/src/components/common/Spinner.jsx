import React from 'react'
import '../../css/spinnerOverlay.css';

const Spinner = () => {
  return (
    <div className="spinner-overlay">
      <div className="d-flex justify-content-center">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    </div>
  );
}

export default Spinner
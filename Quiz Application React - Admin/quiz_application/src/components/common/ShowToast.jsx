import React from 'react';

const ShowToast = ({ type, title, message }) => {
  const toastElement = document.getElementById('dynamicToast');
  const toastHeader = document.getElementById('toastHeader');
  const toastTitle = document.getElementById('toastTitle');
  const toastBody = document.getElementById('toastBody');
  const toastIcon = document.getElementById('toastIcon');

  if (toastElement && toastHeader && toastTitle && toastBody && toastIcon) {
    // Reset classes
    toastElement.className = 'toast position-fixed bottom-0 end-0 m-3'; 
    toastHeader.className = 'toast-header';
    toastIcon.className = 'me-2';
    toastTitle.className = 'me-auto';

    // Apply classes based on toast type
    switch (type) {
      case 'success':
        toastHeader.classList.add('bg-success', 'text-white');
        toastIcon.classList.add('bi', 'bi-check-circle-fill');
        toastTitle.innerText = title || 'Success';
        break;
      case 'error':
        toastHeader.classList.add('bg-danger', 'text-white');
        toastIcon.classList.add('bi', 'bi-exclamation-circle-fill');
        toastTitle.innerText = title || 'Error';
        break;
      case 'info':
        toastHeader.classList.add('bg-info', 'text-white');
        toastIcon.classList.add('bi', 'bi-info-circle-fill');
        toastTitle.innerText = title || 'Info';
        break;
      case 'warning':
        toastHeader.classList.add('bg-warning', 'text-dark');
        toastIcon.classList.add('bi', 'bi-exclamation-triangle-fill');
        toastTitle.innerText = title || 'Warning';
        break;
      default:
        toastHeader.classList.add('bg-primary', 'text-white');
        toastIcon.classList.add('bi', 'bi-info-circle-fill');
        toastTitle.innerText = title || 'Notice';
    }

    toastBody.innerText = message;

    // Remove existing 'show' if any and trigger show again manually
    toastElement.classList.remove('hide');
    toastElement.classList.add('show');

    // Auto hide toast after 3 seconds (optional)
    setTimeout(() => {
      toastElement.classList.remove('show');
      toastElement.classList.add('hide');
    }, 3000);
  }

  return <></>;
};

export default ShowToast;

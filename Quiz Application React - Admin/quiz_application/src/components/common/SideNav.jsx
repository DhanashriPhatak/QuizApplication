import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

const SideNav = () => {
  return (
    <>
      <aside className="app-sidebar bg-body-secondary shadow" data-bs-theme="dark"> {/*begin::Sidebar Brand*/}
        <div className="sidebar-brand"> {/*begin::Brand Link*/} 
          <Link to="/" className="brand-link"> {/*begin::Brand Image*/} <img src="../../dist/assets/img/AdminLTELogo.png" alt="AdminLTE Logo" className="brand-image opacity-75 shadow" /> {/*end::Brand Image*/} {/*begin::Brand Text*/} <span className="brand-text fw-light">AdminLTE 4</span> {/*end::Brand Text*/} </Link> {/*end::Brand Link*/} </div> {/*end::Sidebar Brand*/} {/*begin::Sidebar Wrapper*/}
        <div className="sidebar-wrapper" >
          <nav className="mt-2"> {/*begin::Sidebar Menu*/}
            <ul className="nav sidebar-menu flex-column" data-lte-toggle="treeview" role="menu" data-accordion="false">
              <li className="nav-item menu-open"> <Link to="/" className="nav-link active"> <i className="nav-icon bi bi-speedometer" />
                  <p>
                    Dashboard
                  </p>
                </Link>
              </li>
              <li className="nav-item"> <Link to="/generateQuiz" className="nav-link"> <i className="nav-icon bi bi-patch-question" />
                  <p>Generate a Quiz</p>
                </Link> </li>
                <li className="nav-item"> <Link to="/quizHistory" className="nav-link"> 
                {/* <FontAwesomeIcon icon="fa-solid fa-clock-rotate-left" /> */}
                <i className="nav-icon bi bi-clock-history"></i>
                  <p>Quiz History</p>
                </Link> </li>
              
                  <li className="nav-item">
                  <Link to="/questions" className="nav-link">
                    <i className="nav-icon bi bi-card-text"></i> {/*bi-question-circle*/}
                    <p>Questions</p>
                  </Link>
                </li>
                <li className="nav-item">
                    <Link to="/categories" className="nav-link">
                    <i className="nav-icon bi bi-tags" ></i>
                    <p>Categories</p>
                    </Link>

                </li>
            </ul> {/*end::Sidebar Menu*/}
          </nav>
        </div> {/*end::Sidebar Wrapper*/}
      </aside> {/*end::Sidebar*/}
    </>
  )}

export default SideNav
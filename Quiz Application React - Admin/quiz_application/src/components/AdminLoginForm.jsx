import React, { useContext, useState } from 'react';
import Spinner from './common/Spinner';
import { login } from '../services/AuthService';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from './auth/AuthContext';

const AdminLoginForm = () => {
    const [email,setEmail] = useState('');
    const [password,setPassword] = useState('');
    const [loading,setLoading] = useState(false);
    const navigate = useNavigate();
    // const {storeToken} = useContext(AuthContext);
    const {loginSuccess} = useContext(AuthContext);

    const handleSubmit = (e)=>{
        e.preventDefault();
        console.log({email,password});
        setLoading(true);
        const payload = {
            email:email,
            password:password
        };
        login(payload)
        .then((res)=>{
            console.log("response form login:-",res);
            // localStorage.setItem('token',res.data.token);
            // storeToken(res.data.token);
            loginSuccess(res.data.token);
            navigate('/home');
        })
        .catch((err)=>{
            console.log(err);
        })
        .finally(()=>{
            setLoading(false);
        })
    }

   if (loading) return <Spinner />;
    return (
    <>
         <div className="hold-transition login-page">
      <div className="login-box">
        <div className="login-logo">
          <b>Admin</b>LTE
        </div>
        <div className="card">
          <div className="card-body login-card-body">
            <p className="login-box-msg">Sign in to start your session</p>

            <form onSubmit={handleSubmit}>
              <div className="input-group mb-3">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Username"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="bi bi-envelope" />
                  </div>
                </div>
              </div>

              <div className="input-group mb-3">
                <input
                  type="password"
                  className="form-control"
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="bi bi-lock" />
                  </div>
                </div>
              </div>

              <div className="row">
                {/* Remove "Remember me" for Admin */}
                <div className="col-12">
                  <button type="submit" className="btn btn-primary btn-block">
                    Sign In
                  </button>
                </div>
              </div>
            </form>

            <p className="mb-1 mt-3 text-center">
              <a href="#">I forgot my password</a>
            </p>
          </div>
        </div>
      </div>
    </div>
    </>
  )
}

export default AdminLoginForm;
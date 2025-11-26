import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { login as loginRequest } from "../api/auth";

import "bootstrap/dist/css/bootstrap.min.css";
 
const Login = () => {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({

    usernameOrEmail: "",

    password: "",

  });
 
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [showSuccess, setShowSuccess] = useState(false);
  const [fadeOut, setFadeOut] = useState(false);
  const [fadeIn, setFadeIn] = useState(false);

  // Add CSS animations
  const styleSheet = `
    @keyframes loginFadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }
    @keyframes loginSlideIn {
      from { 
        opacity: 0;
        transform: translate(-50%, -45%);
      }
      to { 
        opacity: 1;
        transform: translate(-50%, -50%);
      }
    }
    @keyframes loginScaleIn {
      0% { 
        transform: scale(0);
      }
      50% {
        transform: scale(1.2);
      }
      100% {
        transform: scale(1);
      }
    }
    @keyframes loginFadeOut {
      from { opacity: 1; }
      to { opacity: 0; }
    }
    @keyframes loginFormSlideUp {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }
    @keyframes loginTitleBounce {
      0% {
        opacity: 0;
        transform: translateY(-50px) scale(0.8);
      }
      60% {
        transform: translateY(5px) scale(1.05);
      }
      100% {
        opacity: 1;
        transform: translateY(0) scale(1);
      }
    }
  `;

  // Inject styles and trigger fade in
  useEffect(() => {
    const style = document.createElement('style');
    style.textContent = styleSheet;
    document.head.appendChild(style);
    
    // Trigger fade in after a short delay
    setTimeout(() => setFadeIn(true), 50);
    
    return () => document.head.removeChild(style);
  }, []);

  const handleChange = (e) => {

    setFormData({ ...formData, [e.target.name]: e.target.value });

  };
 
  const handleSubmit = async (e) => {

    e.preventDefault();
    setErrorMessage("");
    setIsSubmitting(true);
    try {
      const data = await loginRequest({
        usernameOrEmail: formData.usernameOrEmail,
        password: formData.password,
      });
      // Store user data in localStorage
      if (data) {
        localStorage.setItem("user", JSON.stringify(formData.usernameOrEmail));
        localStorage.setItem("isAuthenticated", "true");
      }
      
      // Show success message
      setShowSuccess(true);
      
      // Start fade out after 1.5 seconds
      setTimeout(() => {
        setFadeOut(true);
      }, 1500);
      
      // Navigate to dashboard after animation completes
      setTimeout(() => {
        navigate("/dashboard/home");
      }, 2000);
    } catch (err) {
      setErrorMessage(err?.message || "Login failed. Please try again.");
    } finally {
      setIsSubmitting(false);
    }

  };
 
  const successStyle = {
    position: "fixed",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    backgroundColor: "#fff",
    padding: "2rem 3rem",
    borderRadius: "15px",
    boxShadow: "0 8px 30px rgba(0,0,0,0.3)",
    textAlign: "center",
    zIndex: 1000,
    animation: "loginSlideIn 0.3s ease-out",
  };

  const overlayStyle = {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0,0,0,0.5)",
    zIndex: 999,
    animation: "loginFadeIn 0.3s ease-out",
  };

  const successIconStyle = {
    fontSize: "4rem",
    color: "#28a745",
    marginBottom: "1rem",
    animation: "loginScaleIn 0.5s ease-out",
  };

  const successTextStyle = {
    fontSize: "1.5rem",
    fontWeight: "bold",
    color: "#333",
    marginBottom: "0.5rem",
  };

  const redirectTextStyle = {
    fontSize: "1rem",
    color: "#666",
  };

  const containerStyle = {
    animation: fadeOut ? "loginFadeOut 0.5s ease-out" : "none",
    opacity: fadeOut ? 0 : 1,
  };

  const titleStyle = {
    fontWeight: "bold", 
    fontSize: "3rem", 
    color: "#007bff",
    animation: fadeIn ? "loginTitleBounce 0.6s ease-out" : "none",
    opacity: fadeIn ? 1 : 0,
  };

  const cardStyle = {
    width: "22rem",
    animation: fadeIn ? "loginFormSlideUp 0.6s ease-out 0.2s backwards" : "none",
    opacity: fadeIn ? 1 : 0,
  };

  return (
<div className="d-flex flex-column justify-content-center align-items-center vh-100 bg-light" style={containerStyle}>
      {showSuccess && (
        <>
          <div style={overlayStyle}></div>
          <div style={successStyle}>
            <div style={successIconStyle}>✓</div>
            <div style={successTextStyle}>Login Successful!</div>
            <div style={redirectTextStyle}>Loading your dashboard...</div>
          </div>
        </>
      )}
      
<h1 className="text-center mb-4" style={titleStyle}>
        UNIMATCH
      </h1>
<div className="card shadow-lg p-4" style={cardStyle}>
<h3 className="text-center mb-4">Login</h3>
<form onSubmit={handleSubmit}>
<div className="mb-3">
<label htmlFor="usernameOrEmail" className="form-label">Email or Username</label>
<input

              type="text"

              className="form-control"

              id="usernameOrEmail"

              name="usernameOrEmail"

              placeholder="Enter your email or username"

              value={formData.usernameOrEmail}

              onChange={handleChange}

              required

            />
</div>
 
          <div className="mb-3">
<label htmlFor="password" className="form-label">Password</label>
<input

              type="password"

              className="form-control"

              id="password"

              name="password"

              placeholder="Enter your password"

              value={formData.password}

              onChange={handleChange}

              required

            />
</div>
 
          {errorMessage && (
            <div className="alert alert-danger" role="alert">
              {errorMessage}
            </div>
          )}

          <button type="submit" className="btn btn-primary w-100" disabled={isSubmitting}>

            {isSubmitting ? "Logging in..." : "Login"}
</button>
 
          <div className="text-center mt-3">
<span>Don't have an account? </span>
<a href="/signup" style={{ textDecoration: "none" }}>Sign Up</a>
</div>
</form>
</div>
</div>

  );

};
 
export default Login;

 
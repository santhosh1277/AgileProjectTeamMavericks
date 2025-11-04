import React, { useState } from "react";
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
      // If backend returns a token or user, store as needed
      if (data?.token) {
        localStorage.setItem("authToken", data.token);
      }
      navigate("/");
    } catch (err) {
      setErrorMessage(err?.message || "Login failed. Please try again.");
    } finally {
      setIsSubmitting(false);
    }

  };
 
  const handleSignUp = () => {

    console.log("Redirect to Sign Up page");

  };
 
  return (
<div className="d-flex justify-content-center align-items-center vh-100 bg-light">
<div className="card shadow-lg p-4" style={{ width: "22rem" }}>
<h3 className="text-center mb-4">Login</h3>
<form onSubmit={handleSubmit}>
<div className="mb-3">
<label className="form-label">Email or Username</label>
<input

              type="text"

              className="form-control"

              name="usernameOrEmail"

              placeholder="Enter your email or username"

              value={formData.usernameOrEmail}

              onChange={handleChange}

              required

            />
</div>
 
          <div className="mb-3">
<label className="form-label">Password</label>
<input

              type="password"

              className="form-control"

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
<a href="/signup" onClick={handleSignUp}>Sign Up</a>
</div>
</form>
</div>
</div>

  );

};
 
export default Login;

 
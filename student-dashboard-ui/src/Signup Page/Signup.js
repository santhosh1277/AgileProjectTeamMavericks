import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Signup() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    dob: "",
    email: "",
    phoneNumber: "",
    password: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setErrorMessage(""); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { firstName, lastName, dob, email, phoneNumber, password, confirmPassword } = formData;

    if (!firstName || !lastName || !email || !dob || !phoneNumber || !password) {
      setErrorMessage("All fields are required!");
      return;
    }

    if (password !== confirmPassword) {
      setErrorMessage("Passwords do not match!");
      return;
    }

    setLoading(true);
    setErrorMessage("");

    try {
      const response = await fetch("http://localhost:8080/api/students", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ firstName, lastName, dob, email, phoneNumber, password }),
      });

      if (!response.ok) {
        let errorText = "Failed to register student";
        try {
          const data = await response.json();
          errorText = typeof data === "string" ? data : data.message || errorText;
        } catch {
          const text = await response.text();
          errorText = text || errorText;
        }
        throw new Error(errorText);
      }

      alert("Signup successful! Redirecting to login...");
      navigate("/login");
    } catch (error) {
      console.error(error);
      setErrorMessage(error.message || "Error: Could not register student");
    } finally {
      setLoading(false);
    }
  };

  // Styles
  const containerStyle = {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#1e1e2f",
    padding: "1rem",
  };

  const formStyle = {
    backgroundColor: "#fff",
    padding: "2rem",
    borderRadius: "10px",
    boxShadow: "0 4px 15px rgba(0,0,0,0.2)",
    width: "100%",
    maxWidth: "400px",
  };

  const inputStyle = {
    width: "100%",
    padding: "0.5rem",
    margin: "0.5rem 0 1rem",
    borderRadius: "5px",
    border: "1px solid #ccc",
  };

  const buttonStyle = {
    width: "100%",
    padding: "0.75rem",
    borderRadius: "5px",
    border: "none",
    backgroundColor: "#007bff",
    color: "#fff",
    fontWeight: "bold",
    cursor: loading ? "not-allowed" : "pointer",
  };

  const linkStyle = {
    color: "#007bff",
    textDecoration: "none",
  };

  const errorStyle = {
    color: "#dc3545",
    backgroundColor: "#f8d7da",
    padding: "0.75rem",
    borderRadius: "5px",
    marginBottom: "1rem",
    border: "1px solid #f5c6cb",
  };

  return (
    <div style={containerStyle}>
      <form style={formStyle} onSubmit={handleSubmit}>
        <h2>🎓 Student Signup</h2>

        <label htmlFor="firstName">First Name</label>
        <input
          type="text"
          id="firstName"
          name="firstName"
          placeholder="Enter your First Name"
          value={formData.firstName}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        <label htmlFor="lastName">Last Name</label>
        <input
          type="text"
          id="lastName"
          name="lastName"
          placeholder="Enter your Last Name"
          value={formData.lastName}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        <label htmlFor="dob">Date of Birth</label>
        <input
          type="date"
          id="dob"
          name="dob"
          value={formData.dob}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        <label htmlFor="email">Email Address</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="example@email.com"
          value={formData.email}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        <label htmlFor="phoneNumber">Phone Number</label>
        <input
          type="tel"
          id="phoneNumber"
          name="phoneNumber"
          placeholder="Enter your Phone Number"
          value={formData.phoneNumber}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="********"
          value={formData.password}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        <label htmlFor="confirmPassword">Confirm Password</label>
        <input
          type="password"
          id="confirmPassword"
          name="confirmPassword"
          placeholder="********"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
          style={inputStyle}
        />

        {errorMessage && <div style={errorStyle}>{errorMessage}</div>}

        <button type="submit" style={buttonStyle} disabled={loading}>
          {loading ? "Saving..." : "Create Account"}
        </button>

        <p style={{ marginTop: "1rem", textAlign: "center" }}>
          Already have an account?{" "}
          <a href="/login" style={linkStyle}>
            Login here
          </a>
        </p>
      </form>
    </div>
  );
}

export default Signup;

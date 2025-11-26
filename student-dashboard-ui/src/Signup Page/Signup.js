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
  const [fieldErrors, setFieldErrors] = useState({});
  const [showSuccess, setShowSuccess] = useState(false);
  const navigate = useNavigate();

  // Add CSS animations
  const styleSheet = `
    @keyframes fadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }
    @keyframes slideIn {
      from { 
        opacity: 0;
        transform: translate(-50%, -45%);
      }
      to { 
        opacity: 1;
        transform: translate(-50%, -50%);
      }
    }
    @keyframes scaleIn {
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
  `;

  // Inject styles
  React.useEffect(() => {
    const style = document.createElement('style');
    style.textContent = styleSheet;
    document.head.appendChild(style);
    return () => document.head.removeChild(style);
  }, []);

  // Validation Functions
  const validateName = (name, fieldName) => {
    const trimmedName = name.trim();
    const nameRegex = /^[a-zA-Z\s'-]{2,50}$/;
    
    if (!trimmedName) {
      return `${fieldName} is required`;
    }
    if (trimmedName.length < 2) {
      return `${fieldName} must be at least 2 characters`;
    }
    if (trimmedName.length > 50) {
      return `${fieldName} must be less than 50 characters`;
    }
    if (!nameRegex.test(trimmedName)) {
      return `${fieldName} can only contain letters, spaces, hyphens, and apostrophes`;
    }
    return "";
  };

  const validateEmail = (email) => {
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    
    if (!email) {
      return "Email is required";
    }
    if (!emailRegex.test(email)) {
      return "Please enter a valid email address";
    }
    return "";
  };

  const validatePhoneNumber = (phone) => {
    const digitsOnly = phone.replace(/\D/g, '');
    
    if (!phone) {
      return "Phone number is required";
    }
    if (digitsOnly.length < 10 || digitsOnly.length > 15) {
      return "Phone number must be between 10-15 digits";
    }
    return "";
  };

  const validateDateOfBirth = (dob) => {
    if (!dob) {
      return "Date of birth is required";
    }

    const today = new Date();
    const birthDate = new Date(dob);
    
    if (isNaN(birthDate.getTime())) {
      return "Invalid date format";
    }
    
    if (birthDate > today) {
      return "Date of birth cannot be in the future";
    }

    const age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    const dayDiff = today.getDate() - birthDate.getDate();
    const actualAge = monthDiff < 0 || (monthDiff === 0 && dayDiff < 0) ? age - 1 : age;

    if (actualAge < 13) {
      return "You must be at least 13 years old";
    }
    if (actualAge > 120) {
      return "Invalid date of birth";
    }
    
    return "";
  };

  const validatePassword = (password) => {
    if (!password) {
      return "Password is required";
    }
    if (password.length < 8) {
      return "Password must be at least 8 characters";
    }
    if (password.length > 128) {
      return "Password must be less than 128 characters";
    }
    if (!/[A-Z]/.test(password)) {
      return "Password must contain at least one uppercase letter";
    }
    if (!/[a-z]/.test(password)) {
      return "Password must contain at least one lowercase letter";
    }
    if (!/[0-9]/.test(password)) {
      return "Password must contain at least one number";
    }
    if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
      return "Password must contain at least one special character";
    }
    
    // Check for sequential patterns
    const sequences = ['012', '123', '234', '345', '456', '567', '678', '789', 
                      'abc', 'bcd', 'cde', 'def', 'efg', 'fgh', 'ghi', 'hij', 
                      'ijk', 'jkl', 'klm', 'lmn', 'mno', 'nop', 'opq', 'pqr', 
                      'qrs', 'rst', 'stu', 'tuv', 'uvw', 'vwx', 'wxy', 'xyz'];
    const lowerPassword = password.toLowerCase();
    
    for (const seq of sequences) {
      if (lowerPassword.includes(seq)) {
        return "Password cannot contain sequential patterns (e.g., 123, abc)";
      }
    }
    
    // Check for repeated characters (3+ same character in a row)
    if (/(.)\1{2,}/.test(password)) {
      return "Password cannot contain repeated characters (e.g., aaa, 111)";
    }
    
    return "";
  };

  const validateConfirmPassword = (password, confirmPassword) => {
    if (!confirmPassword) {
      return "Please confirm your password";
    }
    if (password !== confirmPassword) {
      return "Passwords do not match";
    }
    return "";
  };

  const validateField = (name, value) => {
    let error = "";
    
    switch (name) {
      case "firstName":
        error = validateName(value, "First name");
        break;
      case "lastName":
        error = validateName(value, "Last name");
        break;
      case "email":
        error = validateEmail(value);
        break;
      case "phoneNumber":
        error = validatePhoneNumber(value);
        break;
      case "dob":
        error = validateDateOfBirth(value);
        break;
      case "password":
        error = validatePassword(value);
        // Also revalidate confirm password if it has a value
        if (formData.confirmPassword) {
          const confirmError = validateConfirmPassword(value, formData.confirmPassword);
          setFieldErrors(prev => ({ ...prev, confirmPassword: confirmError }));
        }
        break;
      case "confirmPassword":
        error = validateConfirmPassword(formData.password, value);
        break;
      default:
        break;
    }
    
    return error;
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    const error = validateField(name, value);
    setFieldErrors(prev => ({ ...prev, [name]: error }));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setErrorMessage(""); // Clear error when user types
    
    // Clear field error when user starts typing
    if (fieldErrors[name]) {
      setFieldErrors(prev => ({ ...prev, [name]: "" }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { firstName, lastName, dob, email, phoneNumber, password, confirmPassword } = formData;

    // Validate all fields
    const errors = {
      firstName: validateName(firstName, "First name"),
      lastName: validateName(lastName, "Last name"),
      email: validateEmail(email),
      phoneNumber: validatePhoneNumber(phoneNumber),
      dob: validateDateOfBirth(dob),
      password: validatePassword(password),
      confirmPassword: validateConfirmPassword(password, confirmPassword),
    };

    setFieldErrors(errors);

    // Check if there are any errors
    const hasErrors = Object.values(errors).some(error => error !== "");
    if (hasErrors) {
      setErrorMessage("Please fix the errors above before submitting");
      return;
    }

    setLoading(true);
    setErrorMessage("");

    try {
      // Trim names before sending
      const response = await fetch("http://localhost:8080/api/students", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
          firstName: firstName.trim(), 
          lastName: lastName.trim(), 
          dob, 
          email, 
          phoneNumber, 
          password 
        }),
      });

      if (!response.ok) {
        let errorText = "Failed to register student";
        try {
          const text = await response.text();
          // Try to parse as JSON first
          try {
            const data = JSON.parse(text);
            errorText = typeof data === "string" ? data : data.message || errorText;
          } catch {
            // If not JSON, use the text directly
            errorText = text || errorText;
          }
        } catch {
          // If reading fails completely, use default message
          errorText = "Failed to register student";
        }
        throw new Error(errorText);
      }

      // Show success message
      setShowSuccess(true);
      
      // Redirect after 2 seconds
      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (error) {
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
    backgroundColor: (loading || Object.values(fieldErrors).some(error => error !== "")) ? "#6c757d" : "#007bff",
    color: "#fff",
    fontWeight: "bold",
    cursor: (loading || Object.values(fieldErrors).some(error => error !== "")) ? "not-allowed" : "pointer",
    opacity: (loading || Object.values(fieldErrors).some(error => error !== "")) ? 0.6 : 1,
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

  const fieldErrorStyle = {
    color: "#dc3545",
    fontSize: "0.875rem",
    marginTop: "0.25rem",
    marginBottom: "0.5rem",
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
    animation: "slideIn 0.3s ease-out",
  };

  const overlayStyle = {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0,0,0,0.5)",
    zIndex: 999,
    animation: "fadeIn 0.3s ease-out",
  };

  const successIconStyle = {
    fontSize: "4rem",
    color: "#28a745",
    marginBottom: "1rem",
    animation: "scaleIn 0.5s ease-out",
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

  return (
    <div style={containerStyle}>
      {showSuccess && (
        <>
          <div style={overlayStyle}></div>
          <div style={successStyle}>
            <div style={successIconStyle}>✓</div>
            <div style={successTextStyle}>Signup Successful!</div>
            <div style={redirectTextStyle}>Redirecting to login...</div>
          </div>
        </>
      )}
      
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
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.firstName && <div style={fieldErrorStyle}>{fieldErrors.firstName}</div>}

        <label htmlFor="lastName">Last Name</label>
        <input
          type="text"
          id="lastName"
          name="lastName"
          placeholder="Enter your Last Name"
          value={formData.lastName}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.lastName && <div style={fieldErrorStyle}>{fieldErrors.lastName}</div>}

        <label htmlFor="dob">Date of Birth</label>
        <input
          type="date"
          id="dob"
          name="dob"
          value={formData.dob}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.dob && <div style={fieldErrorStyle}>{fieldErrors.dob}</div>}

        <label htmlFor="email">Email Address</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="example@email.com"
          value={formData.email}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.email && <div style={fieldErrorStyle}>{fieldErrors.email}</div>}

        <label htmlFor="phoneNumber">Phone Number</label>
        <input
          type="tel"
          id="phoneNumber"
          name="phoneNumber"
          placeholder="Enter your Phone Number"
          value={formData.phoneNumber}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.phoneNumber && <div style={fieldErrorStyle}>{fieldErrors.phoneNumber}</div>}

        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="********"
          value={formData.password}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.password && <div style={fieldErrorStyle}>{fieldErrors.password}</div>}

        <label htmlFor="confirmPassword">Confirm Password</label>
        <input
          type="password"
          id="confirmPassword"
          name="confirmPassword"
          placeholder="********"
          value={formData.confirmPassword}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
        />
        {fieldErrors.confirmPassword && <div style={fieldErrorStyle}>{fieldErrors.confirmPassword}</div>}

        {errorMessage && <div style={errorStyle}>{errorMessage}</div>}

        <button type="submit" style={buttonStyle} disabled={loading || Object.values(fieldErrors).some(error => error !== "")}>
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

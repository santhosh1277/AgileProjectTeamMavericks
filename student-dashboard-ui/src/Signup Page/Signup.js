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
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    padding: "2rem 1rem",
  };

  const formStyle = {
    background: "rgba(255, 255, 255, 0.95)",
    backdropFilter: "blur(10px)",
    padding: "2.5rem",
    borderRadius: "24px",
    border: "2px solid rgba(255, 255, 255, 0.3)",
    boxShadow: "0 16px 48px rgba(0, 0, 0, 0.2)",
    width: "100%",
    maxWidth: "480px",
  };

  const inputStyle = {
    width: "100%",
    padding: "0.75rem 1rem",
    margin: "0.5rem 0 0.25rem",
    borderRadius: "12px",
    border: "2px solid #e2e8f0",
    fontSize: "15px",
    transition: "all 0.3s ease",
  };

  const buttonStyle = {
    width: "100%",
    padding: "0.875rem",
    marginTop: "1rem",
    borderRadius: "12px",
    border: "none",
    background: (loading || Object.values(fieldErrors).some(error => error !== "")) 
      ? "#a0aec0" 
      : "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    color: "#fff",
    fontWeight: "700",
    fontSize: "16px",
    cursor: (loading || Object.values(fieldErrors).some(error => error !== "")) ? "not-allowed" : "pointer",
    opacity: (loading || Object.values(fieldErrors).some(error => error !== "")) ? 0.6 : 1,
    boxShadow: "0 4px 15px rgba(102, 126, 234, 0.4)",
    transition: "all 0.3s ease",
  };

  const linkStyle = {
    color: "#667eea",
    textDecoration: "none",
    fontWeight: "600",
  };

  const errorStyle = {
    color: "#c53030",
    backgroundColor: "#fed7d7",
    padding: "0.875rem",
    borderRadius: "12px",
    marginBottom: "1rem",
    marginTop: "0.5rem",
    border: "2px solid #fc8181",
    fontSize: "14px",
    fontWeight: "500",
  };

  const fieldErrorStyle = {
    color: "#c53030",
    fontSize: "0.8125rem",
    marginTop: "0.25rem",
    marginBottom: "0.75rem",
    fontWeight: "500",
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
        <h2 style={{
          fontSize: "2rem",
          fontWeight: "700",
          color: "#2d3748",
          marginBottom: "1.5rem",
        }}>🎓 Create Account</h2>

        <label htmlFor="firstName" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>First Name</label>
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
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.firstName && <div style={fieldErrorStyle}>{fieldErrors.firstName}</div>}

        <label htmlFor="lastName" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>Last Name</label>
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
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.lastName && <div style={fieldErrorStyle}>{fieldErrors.lastName}</div>}

        <label htmlFor="dob" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>Date of Birth</label>
        <input
          type="date"
          id="dob"
          name="dob"
          value={formData.dob}
          onChange={handleChange}
          onBlur={handleBlur}
          required
          style={inputStyle}
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.dob && <div style={fieldErrorStyle}>{fieldErrors.dob}</div>}

        <label htmlFor="email" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>Email Address</label>
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
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.email && <div style={fieldErrorStyle}>{fieldErrors.email}</div>}

        <label htmlFor="phoneNumber" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>Phone Number</label>
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
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.phoneNumber && <div style={fieldErrorStyle}>{fieldErrors.phoneNumber}</div>}

        <label htmlFor="password" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>Password</label>
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
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.password && <div style={fieldErrorStyle}>{fieldErrors.password}</div>}

        <label htmlFor="confirmPassword" style={{ fontWeight: "600", color: "#4a5568", fontSize: "14px" }}>Confirm Password</label>
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
          onFocus={(e) => e.target.style.borderColor = "#667eea"}
          onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
        />
        {fieldErrors.confirmPassword && <div style={fieldErrorStyle}>{fieldErrors.confirmPassword}</div>}

        {errorMessage && <div style={errorStyle}>{errorMessage}</div>}

        <button 
          type="submit" 
          style={buttonStyle} 
          disabled={loading || Object.values(fieldErrors).some(error => error !== "")}
          onMouseOver={(e) => {
            if (!loading && !Object.values(fieldErrors).some(error => error !== "")) {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.boxShadow = "0 6px 20px rgba(102, 126, 234, 0.5)";
            }
          }}
          onMouseOut={(e) => {
            e.target.style.transform = "translateY(0)";
            e.target.style.boxShadow = "0 4px 15px rgba(102, 126, 234, 0.4)";
          }}
        >
          {loading ? "Creating Account..." : "Create Account"}
        </button>

        <p style={{ marginTop: "1.5rem", textAlign: "center", fontSize: "15px", color: "#4a5568" }}>
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

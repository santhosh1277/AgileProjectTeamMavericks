import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function SideNav() {
  const navigate = useNavigate();
  const [userName, setUserName] = useState("");
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const [isLoggingOut, setIsLoggingOut] = useState(false);

  // Add CSS animations
  useEffect(() => {
    const styleSheet = `
      @keyframes logoutFadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
      }
      @keyframes logoutSlideIn {
        from { 
          opacity: 0;
          transform: translate(-50%, -45%);
        }
        to { 
          opacity: 1;
          transform: translate(-50%, -50%);
        }
      }
      @keyframes logoutPulse {
        0%, 100% { 
          transform: scale(1);
        }
        50% {
          transform: scale(1.05);
        }
      }
      @keyframes logoutFadeOut {
        from { opacity: 1; }
        to { opacity: 0; }
      }
      @keyframes logoutSlideOut {
        from { 
          opacity: 1;
          transform: translateX(0);
        }
        to { 
          opacity: 0;
          transform: translateX(-100%);
        }
      }
    `;
    
    const style = document.createElement('style');
    style.textContent = styleSheet;
    document.head.appendChild(style);
    
    return () => style.remove();
  }, []);

  // Get user data from localStorage
  useEffect(() => {
    const userData = localStorage.getItem("user");
    if (userData) {
      const user = JSON.parse(userData);
      if (user.name) {
        // Extract first name (first word before space)
        const firstName = user.name.split(" ")[0];
        setUserName(firstName);
      }
    }
  }, []);
  
  const handleLogoutClick = () => {
    setShowLogoutModal(true);
  };

  const handleLogoutConfirm = () => {
    setIsLoggingOut(true);
    
    // Wait for animation to complete
    setTimeout(() => {
      // Clear user data from localStorage
      localStorage.removeItem("user");
      localStorage.removeItem("isAuthenticated");
      // Navigate to home page
      navigate("/");
    }, 1000);
  };

  const handleLogoutCancel = () => {
    setShowLogoutModal(false);
  };

  const modalOverlayStyle = {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0,0,0,0.6)",
    zIndex: 9999,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    animation: "logoutFadeIn 0.3s ease-out",
  };

  const modalStyle = {
    backgroundColor: "#fff",
    padding: "2rem",
    borderRadius: "15px",
    boxShadow: "0 10px 40px rgba(0,0,0,0.3)",
    textAlign: "center",
    maxWidth: "400px",
    width: "90%",
    animation: "logoutSlideIn 0.3s ease-out",
  };

  const modalIconStyle = {
    fontSize: "3.5rem",
    color: "#ffc107",
    marginBottom: "1rem",
    animation: "logoutPulse 1.5s ease-in-out infinite",
  };

  const buttonContainerStyle = {
    display: "flex",
    gap: "1rem",
    marginTop: "1.5rem",
    justifyContent: "center",
  };

  const confirmButtonStyle = {
    padding: "0.6rem 2rem",
    borderRadius: "8px",
    border: "none",
    backgroundColor: "#dc3545",
    color: "#fff",
    fontWeight: "bold",
    cursor: "pointer",
    transition: "all 0.3s ease",
  };

  const cancelButtonStyle = {
    padding: "0.6rem 2rem",
    borderRadius: "8px",
    border: "2px solid #6c757d",
    backgroundColor: "transparent",
    color: "#6c757d",
    fontWeight: "bold",
    cursor: "pointer",
    transition: "all 0.3s ease",
  };

  const navContainerStyle = {
    animation: isLoggingOut ? "logoutSlideOut 1s ease-out" : "none",
    opacity: isLoggingOut ? 0 : 1,
  };

  return (
    <>
      {showLogoutModal && (
        <div style={modalOverlayStyle}>
          <div style={modalStyle}>
            <div style={modalIconStyle}>⚠️</div>
            <h4 style={{ color: "#333", marginBottom: "0.5rem" }}>Confirm Logout</h4>
            <p style={{ color: "#666", marginBottom: "0" }}>Are you sure you want to log out?</p>
            <div style={buttonContainerStyle}>
              <button 
                style={cancelButtonStyle}
                onClick={handleLogoutCancel}
                onMouseEnter={(e) => {
                  e.target.style.backgroundColor = "#6c757d";
                  e.target.style.color = "#fff";
                }}
                onMouseLeave={(e) => {
                  e.target.style.backgroundColor = "transparent";
                  e.target.style.color = "#6c757d";
                }}
              >
                Cancel
              </button>
              <button 
                style={confirmButtonStyle}
                onClick={handleLogoutConfirm}
                onMouseEnter={(e) => e.target.style.backgroundColor = "#c82333"}
                onMouseLeave={(e) => e.target.style.backgroundColor = "#dc3545"}
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      )}
      
    <nav className="d-flex flex-column p-3" style={{ minHeight: "100vh", ...navContainerStyle }}>
      <h4 className="text-center mb-3 border-bottom pb-2" style={{ color: "#fff", fontWeight: "bold" }}>Dashboard</h4>
      {userName && (
        <div 
          className="text-center mb-4" 
          style={{ 
            backgroundColor: "rgba(0, 191, 255, 0.2)",
            border: "2px solid #00BFFF",
            borderRadius: "10px",
            padding: "12px 16px",
            marginTop: "0.5rem",
            boxShadow: "0 2px 8px rgba(0, 191, 255, 0.3)"
          }}
        >
          <p 
            className="mb-0" 
            style={{ 
              fontSize: "1.1rem", 
              color: "#00BFFF",
              fontWeight: "600",
              margin: 0
            }}
          >
             Hi <span style={{ color: "#fff", fontWeight: "bold" }}>{userName}</span>
          </p>
        </div>
      )}
      <ul className="nav flex-column">
          <li className="nav-item">
          <Link className="nav-link text-light" to="/dashboard/home">Home</Link>
        </li>
        <li className="nav-item mb-2">
          <Link className="nav-link text-light" to="/dashboard/profile">Profile</Link>
        </li>
        <li className="nav-item mb-2">
          <Link className="nav-link text-light" to="/dashboard/academicprofile">Academic Profile</Link>
        </li>
        <li className="nav-item mb-2">
          <Link className="nav-link text-light" to="/dashboard/settings">Settings</Link>
        </li>
        <li className="nav-item">
          <button 
            className="nav-link text-light btn btn-link w-100 text-start p-0 ps-3" 
            onClick={handleLogoutClick}
            style={{ textDecoration: 'none' }}
          >
            Logout
          </button>
        </li>
      </ul>
    </nav>
    </>
  );
}

export default SideNav;

import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function SideNav() {
  const [userName, setUserName] = useState("");

  // Get user data from localStorage
  useEffect(() => {
    const userData = localStorage.getItem("user");
    if (userData) {
      try {
        const user = JSON.parse(userData);
        if (user.name) {
          // Extract first name (first word before space)
          const firstName = user.name.split(" ")[0];
          setUserName(firstName);
        }
      } catch (error) {
        console.error("Error parsing user data:", error);
      }
    }
  }, []);

  return (
    <nav className="d-flex flex-column p-3" style={{ minHeight: "100vh" }}>
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
          <Link className="nav-link text-light" to="/dashboard/logout">Logout</Link>
        </li>
      </ul>
    </nav>
  );
}

export default SideNav;

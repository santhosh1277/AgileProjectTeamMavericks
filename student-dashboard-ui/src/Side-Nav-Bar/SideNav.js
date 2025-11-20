import { Link } from "react-router-dom";
<<<<<<< Updated upstream

function SideNav() {
=======
import { useNavigate } from "react-router-dom";


import { useEffect, useState } from "react";

function SideNav() {
  const navigate = useNavigate();
  const handleLogout = (e) => {
    e.preventDefault(); // prevent default link navigation
    sessionStorage.clear(); // clear session storage
    navigate("/"); // redirect to home page
  };
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

>>>>>>> Stashed changes
  return (
    <nav className="d-flex flex-column p-3" style={{ minHeight: "100vh" }}>
      <h4 className="text-center mb-4 border-bottom pb-2">Dashboard</h4>
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
          <Link className="nav-link text-light" to="/" onClick={handleLogout}>Logout</Link>
        </li>
      </ul>
    </nav>
  );
}

export default SideNav;

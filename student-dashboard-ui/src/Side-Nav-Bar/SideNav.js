import { Link } from "react-router-dom";

function SideNav() {
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
          <Link className="nav-link text-light" to="/dashboard/logout">Logout</Link>
        </li>
      </ul>
    </nav>
  );
}

export default SideNav;

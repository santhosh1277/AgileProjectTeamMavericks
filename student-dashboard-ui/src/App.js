import './App.css';
import { BrowserRouter, Route, Routes, useLocation, Navigate } from 'react-router-dom';
import Home from './HomePage/Home';
import Signup from './Signup Page/Signup';
import SideNav from './Side-Nav-Bar/SideNav';
import DashboardHome from './DashboardHome/DashboardHome';
import Login from './LoginPage/Login';
import Profile from './DashboardHome/UserProfile';

function AppWrapper() {
  const location = useLocation();

  const pageClass = location.pathname.startsWith('/dashboard')
    ? 'dashboard-page'
    : location.pathname === '/login' || location.pathname === '/signup'
    ? 'auth-page'
    : 'home-page';
  // const isAuthenticated = !!sessionStorage.getItem("uatToken");
  const isAuthenticated = true; // TEMPORARY: Set to true for testing purposes

  return (
    <div className={pageClass}>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/signup' element={<Signup />} />
        <Route path='/login' element={<Login />} />
        <Route
          path='/dashboard/*'
          element={
            isAuthenticated ? (
              <div className="container-fluid p-0">
                <div className="row min-vh-100">
                  <div className="col-2 bg-dark text-white p-0">
                    <SideNav />
                  </div>
                  <div className="col-10 bg-light p-4">
                    <Routes>
                      <Route path="home" element={<DashboardHome />} />
                      <Route path="profile" element={<Profile />} />
                    </Routes>
                  </div>
                </div>
              </div>
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AppWrapper />
    </BrowserRouter>
  );
}

export default App;

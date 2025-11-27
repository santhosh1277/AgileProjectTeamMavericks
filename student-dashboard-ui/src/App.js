import './App.css';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import Home from './HomePage/Home';
import Signup from './Signup Page/Signup';
import SideNav from './Side-Nav-Bar/SideNav';
import DashboardHome from './DashboardHome/DashboardHome';
import Login from './LoginPage/Login';
import Profile from './DashboardHome/UserProfile';
import CourseDetails from './DashboardHome/CourseDetails';
import ProtectedRoute from './components/ProtectedRoute';
import AcademicProfile from './DashboardHome/AcademicProfile';

function AppWrapper() {
  const location = useLocation();
  const pageClass = location.pathname.startsWith('/dashboard')
    ? 'dashboard-page'
    : location.pathname === '/login' || location.pathname === '/signup'
    ? 'auth-page'
    : 'home-page';

  return (
    <div className={pageClass}>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/signup' element={<Signup />} />
        <Route path='/login' element={<Login />} />
        <Route
          path='/dashboard/*'
          element={
            <ProtectedRoute>
              <div className="container-fluid p-0">
                <div className="row min-vh-100">
                  <div className="col-2 bg-dark text-white p-0">
                    <SideNav />
                  </div>
                  <div className="col-10 bg-light p-4">
                    <Routes>
                      <Route path="/" element={<DashboardHome />} />
                      <Route path="profile" element={<Profile />} />
                      <Route path="courses/:collegeId" element={<CourseDetails />} />
                      <Route path="academicprofile" element={<AcademicProfile />} />
                    </Routes>
                  </div>
                </div>
              </div>
            </ProtectedRoute>
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

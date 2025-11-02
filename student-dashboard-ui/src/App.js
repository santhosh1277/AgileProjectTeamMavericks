import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './HomePage/Home';
import Signup from './Signup Page/Signup';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SideNav from './Side-Nav-Bar/SideNav';
import DashboardHome from './DashboardHome/DashboardHome';
import Login from './LoginPage/Login';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/signup' element={<Signup />} />
        </Routes>
      </div>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/dashboard/*"
          element={
            <div className="container-fluid p-0">
              <div className="row min-vh-100">
                <div className="col-2 bg-dark text-white p-0">
                  <SideNav />
                </div>
                <div className="col-10 bg-light p-4">
                  <Routes>
                    <Route path="home" element={<DashboardHome />} />
                    {/* Add more routes below as needed */}
                    {/* <Route path="profile" element={<Profile />} /> */}
                    {/* <Route path="academicprofile" element={<AcademicProfile />} /> */}
                    {/* <Route path="settings" element={<Settings />} /> */}
                    {/* <Route path="logout" element={<Logout />} /> */}
                  </Routes>
                </div>
              </div>
            </div>
          }
        />
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

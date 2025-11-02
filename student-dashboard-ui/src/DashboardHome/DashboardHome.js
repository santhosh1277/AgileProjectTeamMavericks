import "bootstrap/dist/css/bootstrap.min.css";
import { useEffect, useState } from "react";
import { getCollegesList } from "../api/Service.js/SignupService";

function DashboardHome() {
  const [colleges, setColleges] = useState([]);
  useEffect(() => {
    getCollegesList().then((data) => {
      setColleges(data);
      console.log("Fetched 123:", data);
    }).catch((error) => {
      console.error("Error fetching colleges:", error);
    });
  }, []);
  return (
    <div className="dashboard-home" style={{ width: "100%", height: "100%" }}>

      <div className="bg-dark text-light py-3 text-center rounded-bottom">
        <h2>Student Dashboard</h2>
      </div>
      <div className="container-fluid mt-4">
        <h4 className="mb-3">Upcoming College Admissions</h4>
        <div className="row">
          {colleges?.map((college, index) => (
            <div key={index} className="col-md-6 col-lg-4 mb-4">
              <div className="card shadow-sm h-100 border-0">
                <div className="card-body d-flex flex-column justify-content-between">
                  <div>
                    <h5 className="card-title text-dark">{college.name}</h5>
                    <p className="card-text mb-1">
                      <strong>Courses:</strong> {college.courses.join(", ")}
                    </p>
                    <p className="card-text text-muted">
                      <strong>Location:</strong> {college.location}
                    </p>
                    <p className="card-text text-muted">
                      <strong>World Rank:</strong> {college.worldRank}
                    </p>
                  </div>
                  <button className="btn btn-primary mt-3 w-100">Apply Now</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default DashboardHome;

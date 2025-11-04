import "bootstrap/dist/css/bootstrap.min.css";
import { useEffect, useState } from "react";
import { getCollegesList } from "../api/Service.js/CollegeService";

function DashboardHome() {
  const [colleges, setColleges] = useState([]);
<<<<<<< Updated upstream
  useEffect(() => {
    getCollegesList().then((data) => {
      setColleges(data);
      console.log("Fetched 123:", data);
    }).catch((error) => {
      console.error("Error fetching colleges:", error);
    });
  }, []);
=======
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    getCollegesList()
      .then((data) => {
        setColleges(data || []); 
        setLoading(false);
      })
      .catch((err) => {
        setError("Failed to fetch colleges. Please try again.");
        console.error(err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div className="text-center mt-5">Loading colleges...</div>;
  }

  if (error) {
    return <div className="text-center mt-5 text-danger">{error}</div>;
  }

>>>>>>> Stashed changes
  return (
    <div className="dashboard-home" style={{ width: "100%", height: "100%" }}>

      <div className="bg-dark text-light py-3 text-center rounded-bottom">
        <h2>Student Dashboard</h2>
      </div>
      <div className="container-fluid mt-4">
        <h4 className="mb-3">Upcoming College Admissions</h4>
        <div className="row">
          {colleges.map((college) => (
            <div key={college.id} className="col-md-6 col-lg-4 mb-4">
              <div className="card shadow-sm h-100 border-0">
                <div className="card-body d-flex flex-column justify-content-between">
                  <div>
                    <h5 className="card-title text-dark">{college.name}</h5>
                    <p className="card-text text-muted">
                      <strong>Location:</strong> {college.location}
                    </p>
                    <p className="card-text text-muted">
                      <strong>World Rank:</strong> {college.rank}
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

import "bootstrap/dist/css/bootstrap.min.css";
import { useEffect, useState } from "react";
// Service moved to API folder - using direct API call instead
// import { getCollegesList } from "../api/Service.js/CollegeService";

function DashboardHome() {
  const [colleges, setColleges] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
        const fetchColleges = async () => {
            try {
                const response = await fetch("http://localhost:8080/api/colleges");
                if (!response.ok) {
                    throw new Error("Failed to fetch colleges");
                }
                const data = await response.json();
                setColleges(data);
            } catch (err) {
                setError(err.message || "Error loading colleges");
            } finally {
                setLoading(false);
            }
        };

        fetchColleges();
    }, []);

  if (loading) {
    return <div className="text-center mt-5">Loading colleges...</div>;
  }

  if (error) {
    return <div className="text-center mt-5 text-danger">{error}</div>;
  }

  return (
    <div className="dashboard-home" style={{ width: "100%", minHeight: "100vh", backgroundColor: "#f8f9fa" }}>
      <header className="bg-dark text-light py-3 text-center rounded-bottom">
        <h2>Student Dashboard</h2>
      </header>

      <main className="container-fluid mt-4">
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
      </main>
    </div>
  );
}

export default DashboardHome;

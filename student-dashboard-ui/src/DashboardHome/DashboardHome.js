import "bootstrap/dist/css/bootstrap.min.css";

function DashboardHome() {
  const colleges = [
    { name: "Trinity College Dublin", course: "MSc in Data Science", start: "December 2025" },
    { name: "Technological University of Shannon", course: "MSc in Software Engineering", start: "January 2026" },
    { name: "University College Dublin", course: "MBA in Business Analytics", start: "December 2025" },
    { name: "Cork Institute of Technology", course: "MSc in Artificial Intelligence", start: "January 2026" },
  ];

  return (
    <div className="dashboard-home" style={{ width: "100%", height: "100%" }}>

      <div className="bg-dark text-light py-3 text-center rounded-bottom">
        <h2>Student Dashboard</h2>
      </div>


      <div className="container-fluid mt-4">
        <h4 className="mb-3">Upcoming College Admissions</h4>
        <div className="row">
          {colleges.map((college, index) => (
            <div key={index} className="col-md-6 col-lg-4 mb-4">
              <div className="card shadow-sm h-100 border-0">
                <div className="card-body d-flex flex-column justify-content-between">
                  <div>
                    <h5 className="card-title text-dark">{college.name}</h5>
                    <p className="card-text mb-1">
                      <strong>Course:</strong> {college.course}
                    </p>
                    <p className="card-text text-muted">
                      <strong>Starts:</strong> {college.start}
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

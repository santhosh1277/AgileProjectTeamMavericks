import "bootstrap/dist/css/bootstrap.min.css";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

function CourseDetails() {
  const { collegeId } = useParams();
  const navigate = useNavigate();
  const [college, setCollege] = useState(null);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchCollegeDetails = async () => {
      try {
        setLoading(true);
        setError("");

        // Fetch colleges from external API
        const collegesResponse = await fetch("http://localhost:8080/api/universities?country=Ireland");
        if (!collegesResponse.ok) {
          throw new Error("Failed to fetch college details");
        }

        const collegesData = await collegesResponse.json();
        const collegeIndex = Number.parseInt(collegeId, 10) - 1;
        
        if (collegeIndex < 0 || collegeIndex >= collegesData.length) {
          throw new Error("College not found");
        }

        const selectedUniversity = collegesData[collegeIndex];
        
        // Set college details
        setCollege({
          id: Number.parseInt(collegeId, 10),
          name: selectedUniversity.name,
          location: selectedUniversity.state_province || "Ireland",
          rank: collegeIndex + 1,
          country: selectedUniversity.country
        });

        // Fetch unified master's courses (same for all colleges)
        const coursesResponse = await fetch("http://localhost:8080/api/courses/masters");
        if (!coursesResponse.ok) {
          throw new Error("Failed to fetch courses");
        }

        const coursesData = await coursesResponse.json();
        
        // Get unique courses by name
        const uniqueCourses = coursesData.reduce((acc, course) => {
          if (!acc.find(c => c.name === course.name)) {
            acc.push(course);
          }
          return acc;
        }, []);
        
        setCourses(uniqueCourses);
      } catch (err) {
        setError(err.message || "Error loading college details");
      } finally {
        setLoading(false);
      }
    };

    if (collegeId) {
      fetchCollegeDetails();
    }
  }, [collegeId]);

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" aria-live="polite">
          <span className="visually-hidden">Loading...</span>
        </div>
        <output className="mt-3 d-block">Loading course details...</output>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center mt-5">
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
        <button
          className="btn btn-primary"
          onClick={() => navigate("/dashboard/home")}
        >
          Back to Dashboard
        </button>
      </div>
    );
  }

  return (
    <div
      className="course-details"
      style={{
        width: "100%",
        minHeight: "100vh",
        backgroundColor: "transparent",
      }}
    >
      {/* Header with college information */}
      <header className="bg-dark text-light py-4 px-4 rounded-bottom mb-4">
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <h2 className="mb-2">{college?.name}</h2>
            <p className="mb-0 text-muted">
              {college?.location} • World Rank: {college?.rank}
            </p>
          </div>
          <button
            className="btn btn-outline-light"
            onClick={() => navigate("/dashboard/home")}
          >
            ← Back to Dashboard
          </button>
        </div>
      </header>

      <main className="container-fluid px-4">
        <div className="mb-4">
          <h4 className="mb-3">
            Available Courses at {college?.name}
          </h4>
          <p className="text-muted">
            Explore the programs offered by this institution
          </p>
        </div>

        {/* Course List */}
        {courses.length === 0 ? (
          <div className="alert alert-info" role="alert">
            <h5 className="alert-heading">No Courses Available</h5>
            <p className="mb-0">
              This college currently has no courses listed in our system.
              Please check back later or contact the institution directly.
            </p>
          </div>
        ) : (
          <div className="row">
            {courses.map((course) => (
              <div key={course.id} className="col-md-6 col-lg-4 mb-4">
                <div className="card shadow-sm h-100 border-0">
                  <div className="card-body d-flex flex-column">
                    <h5 className="card-title text-primary">
                      {course.name}
                    </h5>
                    {course.description && (
                      <p className="card-text text-muted flex-grow-1">
                        {course.description}
                      </p>
                    )}
                    <div className="mt-3">
                      <button className="btn btn-outline-primary w-100">
                        Learn More
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Additional Information Section */}
        <div className="mt-5 mb-4">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <h5 className="card-title mb-3">College Information</h5>
              <div className="row">
                <div className="col-md-4 mb-3">
                  <strong>Name:</strong>
                  <p className="text-muted mb-0">{college?.name}</p>
                </div>
                <div className="col-md-4 mb-3">
                  <strong>Location:</strong>
                  <p className="text-muted mb-0">{college?.location}</p>
                </div>
                <div className="col-md-4 mb-3">
                  <strong>World Rank:</strong>
                  <p className="text-muted mb-0">#{college?.rank}</p>
                </div>
                <div className="col-12 mb-3">
                  <strong>Total Courses:</strong>
                  <p className="text-muted mb-0">{courses.length}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default CourseDetails;

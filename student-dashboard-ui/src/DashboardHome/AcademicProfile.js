import React, { useState, useEffect } from "react";
import { AcademicProfile, getRecommendationsByEmail, getUserConsent } from "../Service/StudentService";
import ContactDialog from "./UserConsentDailog";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const CourseRecommendationForm = () => {
  const navigate = useNavigate();
  const [degree, setDegree] = useState("");
  const [interests, setInterests] = useState("");
  const [certifications, setCertifications] = useState("");
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showDialog, setShowDialog] = useState(false);

  const rawUser = localStorage.getItem("user");
  const email = rawUser ? rawUser.replace(/"/g, "") : null;

  // Initialize consent and fetch existing recommendations
  useEffect(() => {
    const init = async () => {
      if (!email) return;

      try {
        const consentGiven = await getUserConsent(email);

if (consentGiven === false) {
  setShowDialog(true);
} else {
  setShowDialog(false); 
}


        // 2️⃣ Fetch existing recommendations
        const existingRecs = await getRecommendationsByEmail(email);
        if (Array.isArray(existingRecs)) {
  setRecommendations(existingRecs);
}
      } catch (err) {
        console.error("Error fetching consent or recommendations:", err);
      }
    };

    init();
  }, [email]);
 const handleRedirect = () => {
    navigate("/dashboard/");
  };
  const callRecommendationService = async () => {
    if (!email) {
      Swal.fire("Email not found in localStorage!");
      return;
    }

    const payload = {
      degree,
      interests: interests.split(",").map((i) => i.trim()).filter(Boolean),
      certifications: certifications.split(",").map((c) => c.trim()).filter(Boolean),
      email,
    };

    setLoading(true);

    try {
      const response = await AcademicProfile(payload);

      if (response) {
        setRecommendations(response); 
        window.location.reload();
      }
    } catch (err) {
      console.error("Error calling service:", err);
      Swal.fire("Failed to get recommendation.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <ContactDialog show={showDialog} onClose={() => setShowDialog(false)} />

      <h3>Course Recommendation</h3>

      <div className="mb-3">
        <label className="form-label">Highest Degree</label>
        <input
          type="text"
          className="form-control"
          value={degree}
          onChange={(e) => setDegree(e.target.value)}
        />
      </div>

      <div className="mb-3">
        <label className="form-label">Interests (comma separated)</label>
        <input
          type="text"
          className="form-control"
          value={interests}
          onChange={(e) => setInterests(e.target.value)}
        />
      </div>

      <div className="mb-3">
        <label className="form-label">Certifications (comma separated)</label>
        <input
          type="text"
          className="form-control"
          value={certifications}
          onChange={(e) => setCertifications(e.target.value)}
        />
      </div>

      <button
        className="btn btn-primary"
        onClick={callRecommendationService}
        disabled={loading}
      >
        {loading ? "Loading..." : "Get Recommendation"}
      </button>

     {/* Recommended Courses */}
<div className="row mt-4">
  {recommendations.length > 0 &&
    recommendations.map((rec, index) => (
      <div className="col-md-12 col-lg-12 mb-4" key={index}>
        <div className="card shadow-sm h-100 course-card border-0">
          
          {/* Badge */}
          <div className="position-absolute top-0 end-0 m-2">
            <span className="badge bg-success px-3 py-2">
              <i className="bi bi-star-fill me-1"></i> Recommended
            </span>
          </div>

          {/* Card Body */}
          <div className="card-body d-flex flex-column">
            <h5 className="card-title fw-bold">{rec.course_name}</h5>
            
            {/* Domain */}
            <p className="text-muted mb-2">
              <i className="bi bi-diagram-3 me-1"></i> {rec.domains}
            </p>

            {/* Description */}
            <p className="card-text text-truncate" style={{ maxHeight: "80px" }}>
              {rec.description}
            </p>

            {/* Skills */}
            {rec.skills && rec.skills.length > 0 && (
              <div className="mt-auto">
                <h6 className="fw-semibold mb-2">
                  <i className="bi bi-tools me-1"></i> Skills you'll gain
                </h6>
                <div className="d-flex flex-wrap gap-2">
                  {rec.skills.map((skill, i) => (
                    <span key={i} className="badge bg-primary">
                      <i className="bi bi-check2-circle me-1"></i> {skill}
                    </span>
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* Footer with action button */}
          <div className="card-footer bg-transparent border-0 mt-auto">
            <button className="btn btn-outline-primary w-100" onClick={handleRedirect}>
              <i className="bi bi-box-arrow-up-right me-1"></i> Search Colleges
            </button>
          </div>
        </div>
      </div>
    ))}
</div>
</div>
  );
};

export default CourseRecommendationForm;

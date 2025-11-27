import React, { useState, useEffect } from "react";
import { AcademicProfile } from "../Service/StudentService";
import ContactDialog from "./UserConsentDailog";   // <-- import dialog

const CourseRecommendationForm = () => {
  const [highestDegree, setDegree] = useState("");
  const [interests, setInterests] = useState("");
  const [certifications, setCertifications] = useState("");
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(false);

  const [showDialog, setShowDialog] = useState(false); // <-- dialog state

  // FIX: Correctly read email (remove extra quotes)
  const rawUser = localStorage.getItem("user"); // "\"sample@gmail.com\""
  const email = rawUser ? rawUser.replace(/"/g, "") : null;

  useEffect(() => {
    // Show the dialog immediately when page opens
    setShowDialog(true);
  }, []);

  const callRecommendationService = async () => {
    if (!email) {
      alert("Email not found in localStorage!");
      return;
    }

    const payload = {
      highestDegree,
      interests: interests.split(",").map((i) => i.trim()).filter(Boolean),
      certifications: certifications.split(",").map((c) => c.trim()).filter(Boolean),
      email,
    };

    setLoading(true);

    try {
      const response = await AcademicProfile(payload);
      setRecommendation(response.data);
    } catch (err) {
      console.error("Error calling service:", err);
      alert("Failed to get recommendation.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <ContactDialog
        show={showDialog}
        onClose={() => setShowDialog(false)}
      />

      <h3>Course Recommendation</h3>

      <div className="mb-3">
        <label className="form-label">Highest Degree</label>
        <input
          type="text"
          className="form-control"
          value={highestDegree}
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

      {recommendation && (
        <div className="mt-4">
          <h5>Recommended Course:</h5>
          <pre>{JSON.stringify(recommendation, null, 2)}</pre>
        </div>
      )}
    </div>
  );
};

export default CourseRecommendationForm;

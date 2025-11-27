import React, { useState, useEffect } from "react";
import axios from "axios";
import { AcademicProfile } from "../Service/StudentService";

const CourseRecommendationForm = () => {
  const [degree, setDegree] = useState("");
  const [interests, setInterests] = useState("");
  const [certifications, setCertifications] = useState("");
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(false);

  const email = localStorage.getItem("user"); // get email from localStorage

  const payload = () => ({
    degree,
    interests: interests.split(",").map((i) => i.trim()), // comma separated
    certifications: certifications.split(",").map((c) => c.trim()),
    email, // add email manually
  });

  const callRecommendationService = async () => {
    if (!email) {
      alert("Email not found in localStorage!");
      return;
    }

    setLoading(true);
    try {
      AcademicProfile(payload());
    } catch (err) {
      console.error("Error calling service:", err);
      alert("Failed to get recommendation.");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    callRecommendationService();
  }, []);

  return (
    <div className="container mt-4">
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

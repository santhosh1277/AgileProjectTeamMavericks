import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Footer from "../components/Footer";

function Home() {
  const [universities, setUniversities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [selectedCountry, setSelectedCountry] = useState("Ireland");

  const countries = [
    "United Kingdom",
    "Ireland",
    "Singapore",
    "Sweden",
    "Switzerland",
    "Netherlands",
    "Australia",
    "Canada",
    "Germany",
    "India",
  ];

  const navigate = useNavigate();

  // 🔁 Fetch universities from your backend
  useEffect(() => {
    const fetchUniversities = async () => {
      try {
        setLoading(true);
        setError("");

        const response = await fetch(
          `http://localhost:8080/api/universities?country=${encodeURIComponent(selectedCountry)}`
        );

        if (!response.ok) {
          throw new Error("Failed to fetch universities");
        }

        const data = await response.json();
        setUniversities(data || []);
      } catch (err) {
        const errorMessage = err?.message || "Failed to fetch universities";
        setError(errorMessage);
      } finally {
        setLoading(false);
      }
    };

    fetchUniversities();
  }, [selectedCountry]);

  return (
    <div
      style={{
        minHeight: "100vh",
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        color: "#000000",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* Top bar */}
      <header 
        className="px-4 py-4" 
        style={{
          background: "rgba(255, 255, 255, 0.95)",
          backdropFilter: "blur(10px)",
          boxShadow: "0 4px 30px rgba(0, 0, 0, 0.1)",
          position: "sticky",
          top: 0,
          zIndex: 1000,
        }}
      >
        <div className="container">
          <div className="row align-items-center">
            <div className="col-3 d-flex align-items-center">
              <div style={{ 
                fontSize: "32px", 
                fontWeight: "800", 
                background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                WebkitBackgroundClip: "text",
                WebkitTextFillColor: "transparent",
                backgroundClip: "text",
              }}>
                🎓 UniMatch
              </div>
            </div>
            <div className="col-6 d-flex justify-content-center align-items-center gap-3">
                <label className="mb-0 fw-bold" style={{ fontSize: "1.1rem", color: "#4a5568" }}>
                  Universities in
                </label>
                <select
                  className="form-select"
                  style={{ 
                    maxWidth: "200px", 
                    height: "42px",
                    borderRadius: "12px",
                    border: "2px solid #e2e8f0",
                    fontWeight: "500",
                    transition: "all 0.3s ease",
                  }}
                  value={selectedCountry}
                  onChange={(e) => setSelectedCountry(e.target.value)}
                  aria-label="Select country"
                  onMouseOver={(e) => e.target.style.borderColor = "#667eea"}
                  onMouseOut={(e) => e.target.style.borderColor = "#e2e8f0"}
                >
                  {countries.map((c) => (
                    <option key={c} value={c}>{c}</option>
                  ))}
                </select>
            </div>
            <div className="col-3 d-flex justify-content-end">
              <button
                className="btn"
                style={{
                  background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                  color: "white",
                  border: "none",
                  borderRadius: "12px",
                  padding: "10px 28px",
                  fontWeight: "600",
                  boxShadow: "0 4px 15px rgba(102, 126, 234, 0.4)",
                  transition: "all 0.3s ease",
                }}
                onClick={() => navigate("/login")}
                onMouseOver={(e) => {
                  e.target.style.transform = "translateY(-2px)";
                  e.target.style.boxShadow = "0 6px 20px rgba(102, 126, 234, 0.5)";
                }}
                onMouseOut={(e) => {
                  e.target.style.transform = "translateY(0)";
                  e.target.style.boxShadow = "0 4px 15px rgba(102, 126, 234, 0.4)";
                }}
              >
                Login
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="container py-5" style={{ flex: 1 }}>

        <div className="row justify-content-center mb-5">
          <div className="col-lg-7 col-md-9 col-sm-11">
            <div style={{ position: "relative" }}>
              <input
                type="text"
                className="form-control"
                placeholder="🔍 Search universities by name, country, or domain..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                style={{
                  height: "56px",
                  borderRadius: "16px",
                  border: "2px solid rgba(255, 255, 255, 0.3)",
                  paddingLeft: "20px",
                  fontSize: "16px",
                  background: "rgba(255, 255, 255, 0.95)",
                  backdropFilter: "blur(10px)",
                  boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
                  transition: "all 0.3s ease",
                }}
                onFocus={(e) => {
                  e.target.style.borderColor = "rgba(255, 255, 255, 0.8)";
                  e.target.style.boxShadow = "0 12px 40px rgba(0, 0, 0, 0.15)";
                }}
                onBlur={(e) => {
                  e.target.style.borderColor = "rgba(255, 255, 255, 0.3)";
                  e.target.style.boxShadow = "0 8px 32px rgba(0, 0, 0, 0.1)";
                }}
              />
            </div>
          </div>
        </div>

        {/* Loading & error */}
        {loading && (
          <div className="text-center">
            <div className="spinner-border" style={{ color: "white", width: "3rem", height: "3rem" }} role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
            <p className="mt-3" style={{ color: "white", fontSize: "18px", fontWeight: "500" }}>Loading universities...</p>
          </div>
        )}

        {error && !loading && (
          <div 
            className="alert alert-danger mx-auto" 
            style={{ 
              maxWidth: "600px", 
              borderRadius: "16px",
              background: "rgba(255, 255, 255, 0.95)",
              border: "2px solid #f56565",
              boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
            }}
          >
            {error}
          </div>
        )}

        {/* University list */}
        {!loading && !error && (
          <div className="row justify-content-center">
            <div className="col-lg-9 col-md-11 col-sm-12 d-flex flex-column gap-4">
              {universities
                .filter((u) =>
                  (u.name || "").toLowerCase().includes(query.toLowerCase()) ||
                  (u.country || "").toLowerCase().includes(query.toLowerCase()) ||
                  (u["state-province"] || "").toLowerCase().includes(query.toLowerCase()) ||
                  (Array.isArray(u.domains) ? u.domains.join(" ") : "")
                    .toLowerCase()
                    .includes(query.toLowerCase())
                )
                .map((u, index) => (
                  <div
                    key={u.name + index}
                    className="d-flex p-4"
                    style={{
                      backgroundColor: "rgba(255, 255, 255, 0.95)",
                      borderRadius: "20px",
                      border: "2px solid rgba(255, 255, 255, 0.3)",
                      boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
                      cursor: u.web_pages && u.web_pages.length > 0 ? "pointer" : "default",
                      transition: "all 0.3s ease",
                      backdropFilter: "blur(10px)",
                    }}
                    onClick={() => {
                      if (u.web_pages && u.web_pages.length > 0) {
                        window.open(u.web_pages[0], "_blank", "noopener,noreferrer");
                      }
                    }}
                    onMouseOver={(e) => {
                      e.currentTarget.style.transform = "translateY(-8px)";
                      e.currentTarget.style.boxShadow = "0 16px 48px rgba(0, 0, 0, 0.2)";
                      e.currentTarget.style.borderColor = "rgba(255, 255, 255, 0.6)";
                    }}
                    onMouseOut={(e) => {
                      e.currentTarget.style.transform = "translateY(0)";
                      e.currentTarget.style.boxShadow = "0 8px 32px rgba(0, 0, 0, 0.1)";
                      e.currentTarget.style.borderColor = "rgba(255, 255, 255, 0.3)";
                    }}
                  >
                    {/* Left: Banner */}
                    <div
                      style={{
                        width: "160px",
                        height: "120px",
                        borderRadius: "16px",
                        background:
                          "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                        marginRight: "24px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        fontSize: "48px",
                        boxShadow: "0 4px 15px rgba(102, 126, 234, 0.3)",
                      }}
                    >
                      🏛️
                    </div>

                    {/* Right: Info */}
                    <div className="flex-grow-1">
                      <div
                        className="small mb-2"
                        style={{ 
                          color: "#a0aec0", 
                          fontWeight: "600",
                          textTransform: "uppercase",
                          letterSpacing: "0.5px",
                          fontSize: "11px",
                        }}
                      >
                        🌍 {u.country || "Ireland"}
                      </div>

                      <h5
                        style={{
                          color: "#2d3748",
                          marginBottom: "10px",
                          fontWeight: "700",
                          fontSize: "1.35rem",
                          lineHeight: "1.3",
                        }}
                      >
                        {u.name}
                      </h5>

                      <div
                        className="small mb-2"
                        style={{ 
                          color: "#718096", 
                          lineHeight: 1.6,
                          fontSize: "14px",
                        }}
                      >
                        {u["state-province"] && (
                          <span className="me-3">
                            📍 {u["state-province"]}
                          </span>
                        )}
                        {u.domains && u.domains.length > 0 && (
                          <span>
                            🌐 {u.domains[0]}
                          </span>
                        )}
                      </div>

                      {u.web_pages && u.web_pages.length > 0 && (
                        <div className="mt-3">
                          <span 
                            style={{
                              display: "inline-block",
                              padding: "6px 16px",
                              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                              color: "white",
                              borderRadius: "8px",
                              fontSize: "13px",
                              fontWeight: "600",
                            }}
                          >
                            Click to visit →
                          </span>
                        </div>
                      )}
                    </div>
                  </div>
                ))}

              {universities.length === 0 && (
                <div 
                  className="text-center mt-5 p-5"
                  style={{
                    background: "rgba(255, 255, 255, 0.9)",
                    borderRadius: "20px",
                    boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <div style={{ fontSize: "48px", marginBottom: "16px" }}>🔍</div>
                  <p style={{ fontSize: "18px", color: "#4a5568", fontWeight: "500" }}>
                    No universities found. Try a different search!
                  </p>
                </div>
              )}
            </div>
          </div>
        )}
      </main>

      {/* Footer added here */}
      <Footer />
    </div>
  );
}

export default Home;

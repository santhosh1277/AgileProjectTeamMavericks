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
        backgroundColor: "#ffffff",
        color: "#000000",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* Top bar */}
      <header className="px-4 py-3 border-bottom border-secondary">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-3 d-flex align-items-center">
              <div style={{ fontSize: "28px", fontWeight: "bold", color: "#0d6efd" }}>
                UniMatch
              </div>
            </div>
            <div className="col-6 d-flex justify-content-center align-items-center gap-2">
              <h2 className="mb-0">Universities in</h2>
              <select
                className="form-select"
                style={{ maxWidth: "240px" }}
                value={selectedCountry}
                onChange={(e) => setSelectedCountry(e.target.value)}
                aria-label="Select country"
              >
                {countries.map((c) => (
                  <option key={c} value={c}>{c}</option>
                ))}
              </select>
            </div>
            <div className="col-3 d-flex justify-content-end">
              <button
                className="btn btn-outline-primary"
                onClick={() => navigate("/login")}
              >
                Login
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="container py-4" style={{ flex: 1 }}>
        
        <div className="row justify-content-center mb-4">
          <div className="col-lg-6 col-md-8 col-sm-10">
            <input
              type="text"
              className="form-control"
              placeholder="Search universities..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />
          </div>
        </div>

        {/* Loading & error */}
        {loading && (
          <p className="text-center text-info">Loading universities...</p>
        )}

        {error && !loading && (
          <p className="text-center text-danger">{error}</p>
        )}

        {/* University list */}
        {!loading && !error && (
          <div className="row justify-content-center">
            <div className="col-lg-8 col-md-10 col-sm-12 d-flex flex-column gap-3">
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
                  className="d-flex p-3"
                  style={{
                    backgroundColor: "#f8f9fa",
                    borderRadius: "16px",
                    border: "1px solid #dee2e6",
                    boxShadow: "0 2px 10px rgba(0, 0, 0, 0.05)",
                    cursor: u.web_pages && u.web_pages.length > 0 ? "pointer" : "default",
                  }}
                  onClick={() => {
                    if (u.web_pages && u.web_pages.length > 0) {
                      window.open(u.web_pages[0], "_blank", "noopener,noreferrer");
                    }
                  }}
                >
                  {/* Left: Banner */}
                  <div
                    style={{
                      width: "140px",
                      height: "100px",
                      borderRadius: "12px",
                      background:
                        "linear-gradient(135deg, #f97316, #facc15)",
                      marginRight: "20px",
                    }}
                  />

                  {/* Right: Info */}
                  <div className="flex-grow-1">
                    <div
                      className="small"
                      style={{ color: "#9ca3af", marginBottom: "4px" }}
                    >
                      {u.name}
                    </div>

                    <h5
                      style={{
                        color: "#000000",
                        marginBottom: "6px",
                        fontWeight: "600",
                      }}
                    >
                      {u.name} – Programs & Info
                    </h5>

                    <div
                      className="small mb-2"
                      style={{ color: "#6c757d", lineHeight: 1.4 }}
                    >
                      {u.country || "Ireland"}
                      {u["state-province"] && ` • ${u["state-province"]}`}
                      {u.domains && u.domains.length > 0 && ` • ${u.domains[0]}`}
                    </div>

                    {/* Card is clickable for redirect; link removed */}
                  </div>
                </div>
              ))}

              {universities.length === 0 && (
                <p className="text-center mt-4">
                  No universities found from the API.
                </p>
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

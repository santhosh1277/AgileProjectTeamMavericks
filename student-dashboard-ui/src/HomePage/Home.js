import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Footer from "../components/Footer";   // ⬅️ IMPORT FOOTER

function Home() {
  const [universities, setUniversities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const navigate = useNavigate();

  // 🔁 Fetch universities from your backend
  useEffect(() => {
    const fetchUniversities = async () => {
      try {
        setLoading(true);
        setError("");

        const response = await fetch(
          "http://localhost:8080/api/universities?country=Ireland"
        );

        if (!response.ok) {
          throw new Error("Failed to fetch universities");
        }

        const data = await response.json();
        setUniversities(data || []);
      } catch (err) {
        setError("Failed to fetch");
      } finally {
        setLoading(false);
      }
    };

    fetchUniversities();
  }, []);

  return (
    <div
      style={{
        minHeight: "100vh",
        backgroundColor: "#000000",
        color: "#ffffff",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* Top bar */}
      <header className="d-flex justify-content-between align-items-center px-4 py-3 border-bottom border-secondary">
        <div style={{ fontSize: "32px", fontWeight: "bold", color: "#00BFFF" }}>
          UniMatch
        </div>
        <button
          className="btn btn-outline-info"
          onClick={() => navigate("/login")}
        >
          Login
        </button>
      </header>

      {/* Main content */}
      <main className="container py-4" style={{ flex: 1 }}>
        <h2 className="text-center mb-4">Universities in Ireland</h2>

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
              {universities.map((u, index) => (
                <div
                  key={u.name + index}
                  className="d-flex p-3"
                  style={{
                    backgroundColor: "#111827",
                    borderRadius: "16px",
                    border: "1px solid #1f2937",
                    boxShadow: "0 2px 10px rgba(0, 0, 0, 0.5)",
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
                        color: "#ffffff",
                        marginBottom: "6px",
                        fontWeight: "600",
                      }}
                    >
                      {u.name} – Programs & Info
                    </h5>

                    <div
                      className="small mb-2"
                      style={{ color: "#d1d5db", lineHeight: 1.4 }}
                    >
                      {u.country || "Ireland"}
                      {u["state-province"] && ` • ${u["state-province"]}`}
                      {u.domains && u.domains.length > 0 && ` • ${u.domains[0]}`}
                    </div>

                    {u.web_pages && u.web_pages.length > 0 && (
                      <a
                        href={u.web_pages[0]}
                        target="_blank"
                        rel="noreferrer"
                        className="small"
                        style={{ color: "#38bdf8", textDecoration: "none" }}
                      >
                        Visit website →
                      </a>
                    )}
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

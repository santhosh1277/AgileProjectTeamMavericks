import "bootstrap/dist/css/bootstrap.min.css";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
// Service moved to API folder - using direct API call instead
// import { getCollegesList } from "../api/Service.js/CollegeService";

const COUNTRIES = [
  "United States",
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
  "Afghanistan",
  "Albania",
  "Algeria",
  "Angola",
  "Antigua and Barbuda",
  "Argentina",
  "Armenia",
  "Austria",
  "Azerbaijan",
  "Bahamas",
  "Bahrain",
  "Bangladesh",
  "Barbados",
  "Belarus",
  "Belgium",
  "Belize",
  "Benin",
  "Bhutan",
  "Bolivia",
  "Bosnia and Herzegovina",
  "Brazil",
  "Bulgaria",
  "Burkina Faso",
  "Burundi",
  "Cambodia",
  "Cameroon",
  "Cape Verde",
  "Central African Republic",
  "Chad",
  "Chile",
  "China",
  "Colombia",
  "Comoros",
  "Congo",
  "Costa Rica",
  "Croatia",
  "Cuba",
  "Cyprus",
  "Czech Republic",
  "Denmark",
  "Djibouti",
  "Dominica",
  "Dominican Republic",
  "Ecuador",
  "Egypt",
  "El Salvador",
  "Equatorial Guinea",
  "Eritrea",
  "Estonia",
  "Eswatini",
  "Ethiopia",
  "Fiji",
  "Finland",
  "France",
  "Gabon",
  "Gambia",
  "Georgia",
  "Ghana",
  "Greece",
  "Grenada",
  "Guatemala",
  "Guinea",
  "Guinea-Bissau",
  "Guyana",
  "Haiti",
  "Honduras",
  "Hungary",
  "Iceland",
  "Indonesia",
  "Iran",
  "Iraq",
  "Israel",
  "Italy",
  "Jamaica",
  "Japan",
  "Jordan",
  "Kazakhstan",
  "Kenya",
  "Kiribati",
  "Kuwait",
  "Kyrgyzstan",
  "Laos",
  "Latvia",
  "Lebanon",
  "Lesotho",
  "Liberia",
  "Libya",
  "Lithuania",
  "Luxembourg",
  "Madagascar",
  "Malawi",
  "Malaysia",
  "Maldives",
  "Mali",
  "Malta",
  "Marshall Islands",
  "Mauritania",
  "Mauritius",
  "Mexico",
  "Micronesia",
  "Moldova",
  "Monaco",
  "Mongolia",
  "Montenegro",
  "Morocco",
  "Mozambique",
  "Myanmar",
  "Namibia",
  "Nauru",
  "Nepal",
  "New Zealand",
  "Nicaragua",
  "Niger",
  "Nigeria",
  "North Korea",
  "North Macedonia",
  "Norway",
  "Oman",
  "Pakistan",
  "Palau",
  "Palestine",
  "Panama",
  "Papua New Guinea",
  "Paraguay",
  "Peru",
  "Philippines",
  "Poland",
  "Portugal",
  "Qatar",
  "Romania",
  "Russia",
  "Rwanda",
  "Saint Kitts and Nevis",
  "Saint Lucia",
  "Saint Vincent and the Grenadines",
  "Samoa",
  "San Marino",
  "Sao Tome and Principe",
  "Saudi Arabia",
  "Senegal",
  "Serbia",
  "Seychelles",
  "Sierra Leone",
  "Slovakia",
  "Slovenia",
  "Solomon Islands",
  "Somalia",
  "South Africa",
  "South Korea",
  "South Sudan",
  "Spain",
  "Sri Lanka",
  "Sudan",
  "Suriname",
  "Syria",
  "Taiwan",
  "Tajikistan",
  "Tanzania",
  "Thailand",
  "Timor-Leste",
  "Togo",
  "Tonga",
  "Trinidad and Tobago",
  "Tunisia",
  "Turkey",
  "Turkmenistan",
  "Tuvalu",
  "Uganda",
  "Ukraine",
  "United Arab Emirates",
  "Uruguay",
  "Uzbekistan",
  "Vanuatu",
  "Vatican City",
  "Venezuela",
  "Vietnam",
  "Yemen",
  "Zambia",
  "Zimbabwe"
];

function DashboardHome() {
  const navigate = useNavigate();
  const [colleges, setColleges] = useState([]);
  const [collegesWithCourses, setCollegesWithCourses] = useState([]); // enriched with courses
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedCountry, setSelectedCountry] = useState("Ireland");
  const [courses, setCourses] = useState([]); // master course list
  const [selectedCourse, setSelectedCourse] = useState("");
  const [fadeIn, setFadeIn] = useState(false);

  // Add CSS animations
  useEffect(() => {
    const styleSheet = `
      @keyframes dashboardFadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
      }
      @keyframes dashboardSlideUp {
        from { 
          opacity: 0;
          transform: translateY(30px);
        }
        to { 
          opacity: 1;
          transform: translateY(0);
        }
      }
      @keyframes cardStaggerIn {
        from {
          opacity: 0;
          transform: translateY(20px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }
    `;
    
    const style = document.createElement('style');
    style.textContent = styleSheet;
    document.head.appendChild(style);
    
    // Trigger fade in after a short delay
    setTimeout(() => setFadeIn(true), 50);
    
    return () => document.head.removeChild(style);
  }, []);

  useEffect(() => {
        const fetchColleges = async () => {
            try {
                setLoading(true);
                setError("");
                // Fetch from external API with selected country
                const response = await fetch(`http://localhost:8080/api/universities?country=${encodeURIComponent(selectedCountry)}`);
                if (!response.ok) {
                    throw new Error("Failed to fetch colleges");
                }
                const data = await response.json();
                
                // Transform the data to match our college structure
                const transformedColleges = data.map((university, index) => ({
                    id: index + 1,
                    name: university.name,
                    location: university.state_province || university.country,
                    rank: index + 1, // Simple ranking based on API order
                    country: university.country,
                    webPages: university.web_pages,
                    domains: university.domains
                }));
                
                setColleges(transformedColleges);
            } catch (err) {
                setError(err.message || "Error loading colleges");
            } finally {
                setLoading(false);
            }
        };

        fetchColleges();
    }, [selectedCountry]);

  // Fetch master courses list once (same endpoint used in CourseDetails)
  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/courses/masters");
        if (!res.ok) return; // silently ignore for dashboard if fails
        const data = await res.json();
        // Deduplicate by name similar to CourseDetails logic
        const unique = data.reduce((acc, course) => {
          if (!acc.find(c => c.name === course.name)) acc.push(course);
          return acc;
        }, []);
        setCourses(unique);
      } catch (_) {
        // ignore errors here; courses filter will just be hidden
      }
    };
    fetchCourses();
  }, []);

  // Enrich colleges with a synthetic subset of courses for filtering purposes
  useEffect(() => {
    if (colleges.length === 0 || courses.length === 0) {
      setCollegesWithCourses(colleges); // fallback
      return;
    }
    // Distribute courses deterministically across colleges
    const perCollege = Math.max(3, Math.floor(courses.length / Math.max(1, Math.min(colleges.length, 10))));
    const enriched = colleges.map((college, idx) => {
      const start = (idx * perCollege) % courses.length;
      const slice = [];
      for (let i = 0; i < perCollege; i++) {
        slice.push(courses[(start + i) % courses.length]);
      }
      return { ...college, courses: slice };
    });
    setCollegesWithCourses(enriched);
  }, [colleges, courses]);

  // Apply course filter
  const filteredColleges = selectedCourse
    ? collegesWithCourses.filter(c => c.courses && c.courses.some(crs => crs.name === selectedCourse))
    : collegesWithCourses;

  if (loading) {
    return (
      <div 
        className="dashboard-home" 
        style={{ 
          width: "100%", 
          minHeight: "100vh", 
          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
          animation: "dashboardFadeIn 0.5s ease-out"
        }}
      >
        <header 
          className="text-light py-4 text-center"
          style={{
            background: "rgba(255, 255, 255, 0.95)",
            backdropFilter: "blur(10px)",
            boxShadow: "0 4px 30px rgba(0, 0, 0, 0.1)",
          }}
        >
          <h2 style={{
            fontSize: "2rem",
            fontWeight: "800",
            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            WebkitBackgroundClip: "text",
            WebkitTextFillColor: "transparent",
            backgroundClip: "text",
          }}>🎓 Student Dashboard</h2>
        </header>
        <div className="text-center mt-5">
          <div className="spinner-border" style={{ color: "white", width: "3rem", height: "3rem" }} role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-3" style={{ color: "white", fontSize: "18px", fontWeight: "500" }}>Loading universities...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div 
        className="dashboard-home" 
        style={{ 
          width: "100%", 
          minHeight: "100vh", 
          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
          animation: "dashboardFadeIn 0.5s ease-out"
        }}
      >
        <header 
          className="text-light py-4 text-center"
          style={{
            background: "rgba(255, 255, 255, 0.95)",
            backdropFilter: "blur(10px)",
            boxShadow: "0 4px 30px rgba(0, 0, 0, 0.1)",
          }}
        >
          <h2 style={{
            fontSize: "2rem",
            fontWeight: "800",
            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            WebkitBackgroundClip: "text",
            WebkitTextFillColor: "transparent",
            backgroundClip: "text",
          }}>🎓 Student Dashboard</h2>
        </header>
        <div className="text-center mt-5">
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
            <i className="bi bi-exclamation-triangle-fill" style={{ fontSize: "2rem" }}></i>
            <p className="mt-3">{error}</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div 
      className="dashboard-home" 
      style={{ 
        width: "100%", 
        minHeight: "100vh", 
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        animation: fadeIn ? "dashboardFadeIn 0.6s ease-out" : "none",
        opacity: fadeIn ? 1 : 0
      }}
    >
      <header 
        className="text-light py-4 text-center"
        style={{
          background: "rgba(255, 255, 255, 0.95)",
          backdropFilter: "blur(10px)",
          boxShadow: "0 4px 30px rgba(0, 0, 0, 0.1)",
          position: "sticky",
          top: 0,
          zIndex: 1000,
          animation: fadeIn ? "dashboardSlideUp 0.5s ease-out" : "none"
        }}
      >
        <h2 style={{
          fontSize: "2rem",
          fontWeight: "800",
          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
          WebkitBackgroundClip: "text",
          WebkitTextFillColor: "transparent",
          backgroundClip: "text",
        }}>🎓 Student Dashboard</h2>
      </header>

      <main 
        className="container-fluid mt-4 px-4"
        style={{
          animation: fadeIn ? "dashboardSlideUp 0.6s ease-out 0.1s backwards" : "none"
        }}
      >
        {/* Country Filter */}
        <div className="row mb-4">
          <div className="col-12">
            <div className="card border-0" style={{
              background: "rgba(255, 255, 255, 0.95)",
              backdropFilter: "blur(10px)",
              borderRadius: "20px",
              boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
            }}>
              <div className="card-body p-4">
                <div className="row gy-3 align-items-center">
                  <div className="col-md-3">
                    <label htmlFor="countryFilter" className="form-label fw-bold mb-0" style={{ color: "#2d3748", fontSize: "1.05rem" }}>
                      🌍 Country
                    </label>
                    <select
                      id="countryFilter"
                      className="form-select form-select-sm mt-2"
                      value={selectedCountry}
                      onChange={(e) => setSelectedCountry(e.target.value)}
                      style={{ 
                        borderRadius: "10px",
                        border: "2px solid #e2e8f0",
                        fontWeight: "500",
                      }}
                      onFocus={(e) => e.target.style.borderColor = "#667eea"}
                      onBlur={(e) => e.target.style.borderColor = "#e2e8f0"}
                    >
                      {COUNTRIES.map((country) => (
                        <option key={country} value={country}>
                          {country}
                        </option>
                      ))}
                    </select>
                  </div>
                  {courses.length > 0 && (
                    <div className="col-md-4">
                      <label htmlFor="courseFilter" className="form-label fw-bold mb-0" style={{ color: "#2d3748", fontSize: "1.05rem" }}>
                        📘 Course
                      </label>
                      <select
                        id="courseFilter"
                        className="form-select form-select-sm mt-2"
                        value={selectedCourse}
                        onChange={(e) => setSelectedCourse(e.target.value)}
                        style={{ 
                          borderRadius: "10px",
                          border: "2px solid #e2e8f0",
                          fontWeight: "500",
                        }}
                        onFocus={(e) => e.target.style.borderColor = "#667eea"}
                        onBlur={(e) => e.target.style.borderColor = "#e2e8f0"}
                      >
                        <option value="">All Courses</option>
                        {courses.map(course => (
                          <option key={course.id || course.name} value={course.name}>{course.name}</option>
                        ))}
                      </select>
                    </div>
                  )}
                  <div className="col-md-5 d-flex align-items-end justify-content-md-end">
                    {selectedCourse && (
                      <button
                        type="button"
                        className="btn btn-sm"
                        style={{
                          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                          color: "white",
                          border: "none",
                          borderRadius: "10px",
                          fontWeight: 600
                        }}
                        onClick={() => setSelectedCourse("")}
                      >
                        Clear Course Filter
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <h4 className="mb-4" style={{ color: "white", fontWeight: "700" }}>
          Universities in {selectedCountry}
          {colleges.length > 0 && <span className="badge ms-2" style={{
            background: "rgba(255, 255, 255, 0.95)",
            color: "#667eea",
            fontSize: "1rem",
            padding: "0.5rem 1rem",
            borderRadius: "12px",
          }}>{colleges.length}</span>}
        </h4>
        
        {filteredColleges.length === 0 ? (
          <div className="alert" role="alert" style={{
            background: "rgba(255, 255, 255, 0.95)",
            backdropFilter: "blur(10px)",
            borderRadius: "20px",
            border: "2px solid rgba(255, 255, 255, 0.3)",
            boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
          }}>
            <h5 className="alert-heading" style={{ color: "#2d3748", fontWeight: "700" }}>No Universities Found</h5>
            <p className="mb-0" style={{ color: "#4a5568" }}>
              No universities found for {selectedCountry}{selectedCourse ? ` offering the course "${selectedCourse}"` : ""}. Try adjusting filters.
            </p>
          </div>
        ) : (
          <div className="row">
          {filteredColleges.map((college, index) => (
            <div 
              key={college.id} 
              className="col-md-6 col-lg-4 mb-4"
              style={{
                animation: fadeIn ? `cardStaggerIn 0.5s ease-out ${0.1 + index * 0.05}s backwards` : "none"
              }}
            >
              <div 
                className="card h-100 border-0"
                style={{ 
                  cursor: "pointer",
                  background: "rgba(255, 255, 255, 0.95)",
                  backdropFilter: "blur(10px)",
                  borderRadius: "20px",
                  boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
                  transition: "all 0.3s ease",
                }}
                onClick={() => navigate(`/dashboard/courses/${college.id}`)}
                onMouseOver={(e) => {
                  e.currentTarget.style.transform = "translateY(-8px)";
                  e.currentTarget.style.boxShadow = "0 16px 48px rgba(0, 0, 0, 0.2)";
                }}
                onMouseOut={(e) => {
                  e.currentTarget.style.transform = "translateY(0)";
                  e.currentTarget.style.boxShadow = "0 8px 32px rgba(0, 0, 0, 0.1)";
                }}
              >
                <div className="card-body d-flex flex-column justify-content-between p-4">
                  <div>
                    <h5 className="card-title" style={{ color: "#2d3748", fontWeight: "700", fontSize: "1.25rem" }}>{college.name}</h5>
                    <p className="card-text" style={{ color: "#718096", marginTop: "0.75rem" }}>
                      <strong>📍 Location:</strong> {college.location}
                    </p>
                    <p className="card-text" style={{ color: "#718096" }}>
                      <strong>🏆 World Rank:</strong> {college.rank}
                    </p>
                    {selectedCourse && college.courses && (
                      <p className="card-text" style={{ color: "#718096" }}>
                        <strong>📘 Courses:</strong> {college.courses.slice(0,3).map(c => c.name).join(", ")}
                      </p>
                    )}
                  </div>
                  <button 
                    className="btn mt-3 w-100"
                    style={{
                      background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                      color: "white",
                      border: "none",
                      borderRadius: "12px",
                      padding: "12px",
                      fontWeight: "600",
                      fontSize: "15px",
                      boxShadow: "0 4px 15px rgba(102, 126, 234, 0.4)",
                      transition: "all 0.3s ease",
                    }}
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate(`/dashboard/courses/${college.id}`);
                    }}
                    onMouseOver={(e) => {
                      e.target.style.transform = "translateY(-2px)";
                      e.target.style.boxShadow = "0 6px 20px rgba(102, 126, 234, 0.5)";
                    }}
                    onMouseOut={(e) => {
                      e.target.style.transform = "translateY(0)";
                      e.target.style.boxShadow = "0 4px 15px rgba(102, 126, 234, 0.4)";
                    }}
                  >
                    View Courses
                  </button>
                </div>
              </div>
            </div>
          ))}
          </div>
        )}
      </main>
    </div>
  );
}

export default DashboardHome;

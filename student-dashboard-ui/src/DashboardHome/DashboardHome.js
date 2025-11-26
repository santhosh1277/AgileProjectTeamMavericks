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
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedCountry, setSelectedCountry] = useState("Ireland");

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

  if (loading) {
    return (
      <div className="dashboard-home" style={{ width: "100%", minHeight: "100vh", backgroundColor: "transparent" }}>
        <header className="bg-dark text-light py-3 text-center rounded-bottom">
          <h2>Student Dashboard</h2>
        </header>
        <div className="text-center mt-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-3">Loading universities...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard-home" style={{ width: "100%", minHeight: "100vh", backgroundColor: "transparent" }}>
        <header className="bg-dark text-light py-3 text-center rounded-bottom">
          <h2>Student Dashboard</h2>
        </header>
        <div className="text-center mt-5 text-danger">
          <i className="bi bi-exclamation-triangle-fill" style={{ fontSize: "3rem" }}></i>
          <p className="mt-3">{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-home" style={{ width: "100%", minHeight: "100vh", backgroundColor: "transparent" }}>
      <header className="bg-dark text-light py-3 text-center rounded-bottom">
        <h2>Student Dashboard</h2>
      </header>

      <main className="container-fluid mt-4">
        {/* Country Filter */}
        <div className="row mb-4">
          <div className="col-12">
            <div className="card shadow-sm border-0">
              <div className="card-body">
                <div className="row align-items-center">
                  <div className="col-md-3">
                    <label htmlFor="countryFilter" className="form-label fw-bold mb-0">
                      Select Country:
                    </label>
                  </div>
                  <div className="col-md-9">
                    <select
                      id="countryFilter"
                      className="form-select form-select-lg"
                      value={selectedCountry}
                      onChange={(e) => setSelectedCountry(e.target.value)}
                      style={{ maxWidth: "400px" }}
                    >
                      {COUNTRIES.map((country) => (
                        <option key={country} value={country}>
                          {country}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <h4 className="mb-3">
          Universities in {selectedCountry}
          {colleges.length > 0 && <span className="badge bg-primary ms-2">{colleges.length}</span>}
        </h4>
        
        {colleges.length === 0 ? (
          <div className="alert alert-info" role="alert">
            <h5 className="alert-heading">No Universities Found</h5>
            <p className="mb-0">
              No universities found for {selectedCountry}. Try selecting a different country from the filter above.
            </p>
          </div>
        ) : (
          <div className="row">
          {colleges.map((college) => (
            <div key={college.id} className="col-md-6 col-lg-4 mb-4">
              <div 
                className="card shadow-sm h-100 border-0"
                style={{ cursor: "pointer" }}
                onClick={() => navigate(`/dashboard/courses/${college.id}`)}
              >
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
                  <button 
                    className="btn btn-primary mt-3 w-100"
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate(`/dashboard/courses/${college.id}`);
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

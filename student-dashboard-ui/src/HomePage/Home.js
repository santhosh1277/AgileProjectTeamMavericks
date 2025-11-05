import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Home() {
  const [colleges, setColleges] = useState([]);
  const [search, setSearch] = useState("");
  const [filteredColleges, setFilteredColleges] = useState([]);
  const [selectedCollege, setSelectedCollege] = useState(null);
  const navigate = useNavigate();

  // Fetch colleges from backend
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/colleges")
      .then((response) => {
        setColleges(response.data);
        setFilteredColleges(response.data);
      })
      .catch((error) => {
        console.error("Error fetching colleges:", error);
      });
  }, []);

  // Filter colleges when search changes
  useEffect(() => {
    if (search.trim() === "") {
      setFilteredColleges(colleges);
    } else {
      setFilteredColleges(
        colleges.filter((college) =>
          college.name.toLowerCase().includes(search.toLowerCase())
        )
      );
    }
  }, [search, colleges]);

  return (
    <div
      style={{
        backgroundColor: "black",
        height: "100vh",
        width: "100vw",
        color: "white",
        padding: "20px",
        boxSizing: "border-box",
      }}
    >
      {/* Header */}
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "20px",
        }}
      >
        <h1 style={{ color: "#00BFFF", margin: 0 }}>UniMatch</h1>
        <button
          onClick={() => navigate("/login")}
          style={{
            backgroundColor: "transparent",
            color: "#00BFFF",
            border: "1px solid #00BFFF",
            padding: "8px 16px",
            borderRadius: "8px",
            cursor: "pointer",
          }}
        >
          Login
        </button>
      </div>

      <hr style={{ borderColor: "#00BFFF", marginBottom: "30px" }} />

      {/* Title */}
      <h2 style={{ textAlign: "center", marginBottom: "20px" }}>
        Welcome to Student Dashboard
      </h2>

      {/* Search Dropdown */}
      <div style={{ textAlign: "center", marginBottom: "30px" }}>
        <input
          type="text"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search colleges..."
          style={{
            width: "300px",
            padding: "10px",
            borderRadius: "8px",
            border: "1px solid #00BFFF",
            backgroundColor: "black",
            color: "white",
            outline: "none",
          }}
        />

        {/* Dropdown List */}
        {search && (
          <div
            style={{
              margin: "0 auto",
              width: "300px",
              backgroundColor: "#111",
              border: "1px solid #00BFFF",
              borderRadius: "8px",
              maxHeight: "200px",
              overflowY: "auto",
              marginTop: "5px",
              textAlign: "left",
              zIndex: 10,
              position: "relative",
            }}
          >
            {filteredColleges.length > 0 ? (
              filteredColleges.map((college) => (
                <div
                  key={college.id}
                  onClick={() => setSelectedCollege(college)}
                  style={{
                    padding: "8px 12px",
                    cursor: "pointer",
                    borderBottom: "1px solid #222",
                  }}
                  onMouseEnter={(e) =>
                    (e.target.style.backgroundColor = "#222")
                  }
                  onMouseLeave={(e) =>
                    (e.target.style.backgroundColor = "transparent")
                  }
                >
                  {college.name}
                </div>
              ))
            ) : (
              <div style={{ padding: "8px 12px", color: "#aaa" }}>
                No results found
              </div>
            )}
          </div>
        )}
      </div>

      {/* Selected College Details */}
      {selectedCollege && (
        <div
          style={{
            width: "70%",
            margin: "0 auto",
            border: "1px solid #00BFFF",
            borderRadius: "8px",
            padding: "20px",
            backgroundColor: "#111",
          }}
        >
          <h3 style={{ color: "#00BFFF" }}>{selectedCollege.name}</h3>
          <p>
            <strong>Location:</strong> {selectedCollege.location}
          </p>
          <p>
            <strong>Rank:</strong> {selectedCollege.rank}
          </p>
        </div>
      )}
    </div>
  );
}

export default Home;

import React, { useState, useEffect, useCallback } from "react";
import { FaPen } from "react-icons/fa";
import { GetStudentDetails, UpdateStudentDetails } from "../Service/StudentService";

const defaultEditable = {
  firstName: false,
  lastName: false,
  dob: false,
  phone: false,
  password: false,
};

const emptyUser = {
  firstName: "",
  lastName: "",
  dob: "",
  phone: "",
  password: "",
};

const fields = [
  { label: "First Name", name: "firstName", type: "text" },
  { label: "Last Name", name: "lastName", type: "text" },
  { label: "Date of Birth", name: "dob", type: "date" },
  { label: "Phone Number", name: "phone", type: "tel" },
  { label: "Password", name: "password", type: "password" },
];

const Profile = () => {
  const [isEditable, setIsEditable] = useState(defaultEditable);
  const [user, setUser] = useState(emptyUser);
  const [loading, setLoading] = useState(true);

  // Fetch student details
  const fetchStudentDetails = useCallback(async () => {
    try {
      const email = JSON.parse(localStorage.getItem("user"));
      const data = await GetStudentDetails(email);
      setUser({
        firstName: data.firstName || "",
        lastName: data.lastName || "",
        dob: data.dob || "",
        email: data.email || "",
        phone: data.phoneNumber || "",
        password: "••••••••", // Don't show the encrypted password
      });
    } catch (error) {
      setUser(emptyUser);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchStudentDetails();
  }, [fetchStudentDetails]);


  const handleEdit = (field) => {
    setIsEditable((prev) => ({
      ...prev,
      [field]: !prev[field],
    }));
  };


  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prev) => ({ ...prev, [name]: value }));
  };

  const UpdateDetails = async () => {
    try {
      const updatedUser = {
        ...user,
        phoneNumber: user.phone,
      };
      
      // Only update password if it was actually changed (not the placeholder)
      if (user.password === "••••••••") {
        delete updatedUser.password;
      }
      
      const updated = await UpdateStudentDetails(updatedUser);
      setUser({
        firstName: updated.firstName || "",
        lastName: updated.lastName || "",
        dob: updated.dob || "",
        email: updated.email || "",
        phone: updated.phoneNumber || "",
        password: "••••••••", // Keep showing placeholder
      });
      setIsEditable(defaultEditable);
    } catch {
      // Keep existing state on error - user can retry by editing again
      setIsEditable(defaultEditable);
    }
  };

  if (loading) return <div className="text-center mt-5">Loading...</div>;

  return (
    <div className="container mt-5 d-flex justify-content-center">
      <div className="card shadow p-4 mb-4" style={{ width: "800px" }}>
        
        {fields.map(({ label, name, type }) => (
          <div className="mb-3" key={name}>
            <label htmlFor={name} className="form-label">
              {label}
            </label>

            <div className="d-flex align-items-center">
              <input
                id={name}
                type={type}
                className="form-control"
                name={name}
                value={user[name]}
                disabled={!isEditable[name]}
                onChange={handleChange}
              />

              <button
                type="button"
                data-testid={`edit-${name}`}
                className="btn btn-outline-secondary ms-2"
                aria-label={`Edit ${label}`}
                onClick={() => handleEdit(name)}
              >
                <FaPen />
              </button>
            </div>
          </div>
        ))}

        <div className="d-flex justify-content-center mt-4">
          <button className="btn btn-primary" onClick={UpdateDetails}>
            Save Changes
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
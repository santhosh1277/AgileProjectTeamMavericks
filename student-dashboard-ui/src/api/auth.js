import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

export async function login({ usernameOrEmail, password }) {
  try {
    const payload = { usernameOrEmail, password };
    const response = await axios.post(`${API_BASE_URL}/api/students/login`, payload, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    // Handle error response from backend
    if (error.response) {
      const errorMessage = error.response.data || error.response.data?.message || "Login failed. Please try again.";
      throw new Error(typeof errorMessage === 'string' ? errorMessage : "Login failed. Please try again.");
    }
    throw new Error("Network error. Please check your connection.");
  }
}




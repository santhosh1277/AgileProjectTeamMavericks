import axios from "axios";

const API_BASE_URL = "";

export const login = async ({ usernameOrEmail, password }) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/login`, {
      usernameOrEmail,
      password,
    });
    const { uatToken } = response.data;

    if (!uatToken) {
      throw new Error("No UAT token received from server");
    }

    return { uatToken };
  } catch (error) {
    const message =
      error.response?.data?.message || error.message || "Login failed";
    throw new Error(message);
  }
};

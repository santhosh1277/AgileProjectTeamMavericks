import apiClient from "./client";

export async function login({ usernameOrEmail, password }) {
  const payload = { usernameOrEmail, password };
  const response = await apiClient.post("/auth/login", payload);
  return response.data;
}



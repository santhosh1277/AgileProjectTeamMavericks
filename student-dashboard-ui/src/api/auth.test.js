import axios from "axios";
import { login } from "./auth";

jest.mock("axios");

describe("auth service", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe("login", () => {
    test("successfully logs in with valid credentials", async () => {
      const mockResponse = { data: { id: 1, name: "Test User" } };
      axios.post.mockResolvedValueOnce(mockResponse);

      const result = await login({
        usernameOrEmail: "test@example.com",
        password: "password123",
      });

      expect(axios.post).toHaveBeenCalledWith(
        "http://localhost:8080/api/students/login",
        { usernameOrEmail: "test@example.com", password: "password123" },
        { headers: { "Content-Type": "application/json" } }
      );
      expect(result).toEqual({ id: 1, name: "Test User" });
    });

    test("throws error on failed login with error response", async () => {
      axios.post.mockRejectedValueOnce({
        response: { data: "Invalid credentials" },
      });

      await expect(
        login({ usernameOrEmail: "test@example.com", password: "wrong" })
      ).rejects.toThrow("Invalid credentials");
    });

    test("throws network error when no response", async () => {
      axios.post.mockRejectedValueOnce(new Error("Network Error"));

      await expect(
        login({ usernameOrEmail: "test@example.com", password: "password123" })
      ).rejects.toThrow("Network error. Please check your connection.");
    });
  });
});

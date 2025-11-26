import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Signup from "./Signup";

// Mock useNavigate
const mockNavigate = jest.fn();

jest.mock(
  "react-router-dom",
  () => ({
    useNavigate: () => mockNavigate,
  }),
  { virtual: true }
);

// Mock fetch
global.fetch = jest.fn();

// Mock alert
global.alert = jest.fn();

describe("Signup Component", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const renderSignup = () => {
    return render(<Signup />);
  };

  test("renders signup form with all fields", () => {
    renderSignup();

    expect(screen.getByText(/Student Signup/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/First Name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Last Name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Date of Birth/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Email Address/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Phone Number/i)).toBeInTheDocument();
    expect(screen.getByLabelText("Password")).toBeInTheDocument();
    expect(screen.getByLabelText(/Confirm Password/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Create Account/i })).toBeInTheDocument();
  });

  test("updates form fields on input change", () => {
    renderSignup();

    const firstNameInput = screen.getByLabelText(/First Name/i);
    const emailInput = screen.getByLabelText(/Email Address/i);

    fireEvent.change(firstNameInput, { target: { value: "John" } });
    fireEvent.change(emailInput, { target: { value: "john@example.com" } });

    expect(firstNameInput.value).toBe("John");
    expect(emailInput.value).toBe("john@example.com");
  });

  test("shows error when required fields are missing", async () => {
    renderSignup();

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/All fields are required!/i)).toBeInTheDocument();
    });
  });

  test("shows error when passwords do not match", async () => {
    renderSignup();

    fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: "John" } });
    fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: "Doe" } });
    fireEvent.change(screen.getByLabelText(/Date of Birth/i), { target: { value: "2000-01-01" } });
    fireEvent.change(screen.getByLabelText(/Email Address/i), { target: { value: "john@example.com" } });
    fireEvent.change(screen.getByLabelText(/Phone Number/i), { target: { value: "1234567890" } });
    fireEvent.change(screen.getByLabelText("Password"), { target: { value: "password123" } });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), { target: { value: "password456" } });

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/Passwords do not match!/i)).toBeInTheDocument();
    });
  });

  test("clears error message when user types", async () => {
    renderSignup();

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/All fields are required!/i)).toBeInTheDocument();
    });

    fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: "John" } });

    expect(screen.queryByText(/All fields are required!/i)).not.toBeInTheDocument();
  });

  test("submits form successfully and navigates to login", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ message: "Success" }),
    });

    renderSignup();

    fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: "John" } });
    fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: "Doe" } });
    fireEvent.change(screen.getByLabelText(/Date of Birth/i), { target: { value: "2000-01-01" } });
    fireEvent.change(screen.getByLabelText(/Email Address/i), { target: { value: "john@example.com" } });
    fireEvent.change(screen.getByLabelText(/Phone Number/i), { target: { value: "1234567890" } });
    fireEvent.change(screen.getByLabelText("Password"), { target: { value: "password123" } });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), { target: { value: "password123" } });

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/students", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          firstName: "John",
          lastName: "Doe",
          dob: "2000-01-01",
          email: "john@example.com",
          phoneNumber: "1234567890",
          password: "password123",
        }),
      });
    });

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith("Signup successful! Redirecting to login...");
      expect(mockNavigate).toHaveBeenCalledWith("/login");
    });
  });

  test("displays error message on failed signup", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: false,
      text: async () => "Email already exists",
    });

    renderSignup();

    fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: "John" } });
    fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: "Doe" } });
    fireEvent.change(screen.getByLabelText(/Date of Birth/i), { target: { value: "2000-01-01" } });
    fireEvent.change(screen.getByLabelText(/Email Address/i), { target: { value: "john@example.com" } });
    fireEvent.change(screen.getByLabelText(/Phone Number/i), { target: { value: "1234567890" } });
    fireEvent.change(screen.getByLabelText("Password"), { target: { value: "password123" } });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), { target: { value: "password123" } });

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/Email already exists/i)).toBeInTheDocument();
    });
  });

  test("shows loading state during submission", async () => {
    global.fetch.mockImplementationOnce(
      () => new Promise((resolve) => setTimeout(() => resolve({ ok: true }), 100))
    );

    renderSignup();

    fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: "John" } });
    fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: "Doe" } });
    fireEvent.change(screen.getByLabelText(/Date of Birth/i), { target: { value: "2000-01-01" } });
    fireEvent.change(screen.getByLabelText(/Email Address/i), { target: { value: "john@example.com" } });
    fireEvent.change(screen.getByLabelText(/Phone Number/i), { target: { value: "1234567890" } });
    fireEvent.change(screen.getByLabelText("Password"), { target: { value: "password123" } });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), { target: { value: "password123" } });

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    expect(screen.getByRole("button", { name: /Saving.../i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Saving.../i })).toBeDisabled();

    await waitFor(() => {
      expect(screen.queryByRole("button", { name: /Saving.../i })).not.toBeInTheDocument();
    });
  });

  test("handles network error gracefully", async () => {
    global.fetch.mockRejectedValueOnce(new Error("Network error"));

    console.error = jest.fn();

    renderSignup();

    fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: "John" } });
    fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: "Doe" } });
    fireEvent.change(screen.getByLabelText(/Date of Birth/i), { target: { value: "2000-01-01" } });
    fireEvent.change(screen.getByLabelText(/Email Address/i), { target: { value: "john@example.com" } });
    fireEvent.change(screen.getByLabelText(/Phone Number/i), { target: { value: "1234567890" } });
    fireEvent.change(screen.getByLabelText("Password"), { target: { value: "password123" } });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), { target: { value: "password123" } });

    const submitButton = screen.getByRole("button", { name: /Create Account/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/Network error/i)).toBeInTheDocument();
    });

    expect(console.error).toHaveBeenCalled();
  });

  test("renders login link", () => {
    renderSignup();

    const loginLink = screen.getByText(/Login here/i);
    expect(loginLink).toBeInTheDocument();
    expect(loginLink).toHaveAttribute("href", "/login");
  });
});

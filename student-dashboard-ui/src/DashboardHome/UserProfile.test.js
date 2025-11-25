import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Profile from "./UserProfile";
import { GetStudentDetails, UpdateStudentDetails } from "../Service/StudentService";

jest.mock("../Service/StudentService");

describe("Profile Component", () => {
  const mockStudentData = {
    firstName: "John",
    lastName: "Doe",
    dob: "2000-01-01",
    email: "john@example.com",
    phone: "1234567890",
    password: "abc123",
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("displays loading initially", async () => {
    GetStudentDetails.mockResolvedValueOnce(mockStudentData);
    render(<Profile />);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  test("fetches and displays student details", async () => {
    GetStudentDetails.mockResolvedValueOnce(mockStudentData);

    render(<Profile />);
    const firstNameInput = await screen.findByDisplayValue("John");

    expect(firstNameInput).toBeInTheDocument();
    expect(screen.getByDisplayValue("Doe")).toBeInTheDocument();
    expect(screen.getByDisplayValue("2000-01-01")).toBeInTheDocument();
    expect(screen.getByDisplayValue("john@example.com")).toBeInTheDocument();
    expect(screen.getByDisplayValue("1234567890")).toBeInTheDocument();
    expect(screen.getByDisplayValue("abc123")).toBeInTheDocument();
  });

  test("all fields are initially disabled", async () => {
    GetStudentDetails.mockResolvedValueOnce(mockStudentData);

    render(<Profile />);

    const firstNameInput = await screen.findByDisplayValue("John");
    expect(firstNameInput).toBeDisabled();

    expect(screen.getByDisplayValue("Doe")).toBeDisabled();
    expect(screen.getByDisplayValue("2000-01-01")).toBeDisabled();
    expect(screen.getByDisplayValue("john@example.com")).toBeDisabled();
    expect(screen.getByDisplayValue("1234567890")).toBeDisabled();
    expect(screen.getByDisplayValue("abc123")).toBeDisabled();
  });

  test("edit button toggles input field", async () => {
    GetStudentDetails.mockResolvedValueOnce(mockStudentData);

    render(<Profile />);

    const firstNameInput = await screen.findByDisplayValue("John");
    const editBtn = screen.getByTestId("edit-firstName");

    expect(firstNameInput).toBeDisabled();
    fireEvent.click(editBtn);
    expect(firstNameInput).not.toBeDisabled();

    fireEvent.click(editBtn);
    expect(firstNameInput).toBeDisabled();
  });

  test("typing in input updates value", async () => {
    GetStudentDetails.mockResolvedValueOnce(mockStudentData);

    render(<Profile />);

    const firstNameInput = await screen.findByDisplayValue("John");
    fireEvent.click(screen.getByTestId("edit-firstName"));

    fireEvent.change(firstNameInput, { target: { value: "Sam" } });
    expect(firstNameInput.value).toBe("Sam");
  });

test("clicking save calls UpdateStudentDetails and resets edit fields", async () => {
  GetStudentDetails.mockResolvedValueOnce(mockStudentData); 
  UpdateStudentDetails.mockResolvedValueOnce({
    ...mockStudentData,
    firstName: "Sam",
  });
  render(<Profile />);
  const firstNameInput = await screen.findByDisplayValue("John");
  fireEvent.click(screen.getByTestId("edit-firstName"));
  fireEvent.change(firstNameInput, { target: { value: "Sam" } });

  const saveBtn = screen.getByRole("button", { name: /save changes/i });
  fireEvent.click(saveBtn);

  await waitFor(() =>
    expect(UpdateStudentDetails).toHaveBeenCalledWith({
      firstName: "Sam",
      lastName: mockStudentData.lastName,
      dob: mockStudentData.dob,
      email: mockStudentData.email,
      phone: mockStudentData.phone,
      password: mockStudentData.password,
    })
  );

  await waitFor(() => {
    const updatedInput = screen.getByDisplayValue("Sam");
    expect(updatedInput).toBeDisabled();
  });
});





  test("handles error if UpdateStudentDetails fails", async () => {
    GetStudentDetails.mockResolvedValueOnce(mockStudentData);
    UpdateStudentDetails.mockRejectedValueOnce(new Error("API Error"));

    console.error = jest.fn();

    render(<Profile />);

    const firstNameInput = await screen.findByDisplayValue("John");
    fireEvent.click(screen.getByTestId("edit-firstName"));
    fireEvent.change(firstNameInput, { target: { value: "Sam" } });

    const saveBtn = screen.getByRole("button", { name: /save changes/i });

    await waitFor(() => fireEvent.click(saveBtn));

    expect(console.error).toHaveBeenCalledWith(
      "Error updating student details:",
      expect.any(Error)
    );
  });

  test("handles error if GetStudentDetails fails", async () => {
    GetStudentDetails.mockRejectedValueOnce(new Error("Fetch Error"));

    console.error = jest.fn();

    render(<Profile />);

    await waitFor(() => {
      expect(console.error).toHaveBeenCalledWith(
        "Error fetching student details:",
        expect.any(Error)
      );
    });
  });
});
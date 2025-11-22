import { UpdateStudentDetails, GetStudentDetails } from "./StudentService";

global.fetch = jest.fn();

describe("StudentService API functions", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe("UpdateStudentDetails", () => {
    const mockStudentData = {
      firstName: "John",
      lastName: "Doe",
      dob: "2000-01-01",
      email: "john@example.com",
      phone: "1234567890",
      password: "abc123",
    };

    test("should update student and return data on success", async () => {
      const mockResponse = { ...mockStudentData, firstName: "Sam" };
      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockResponse,
      });

      const result = await UpdateStudentDetails(mockStudentData);

      expect(fetch).toHaveBeenCalledWith("http://localhost:8080/api/Student", {
        method: "Post",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(mockStudentData),
      });

      expect(result).toEqual(mockResponse);
    });

    test("should throw error if response not ok", async () => {
      fetch.mockResolvedValueOnce({ ok: false });

      await expect(UpdateStudentDetails(mockStudentData)).rejects.toThrow(
        "Failed to Update Student Details"
      );
    });

    test("should throw error if fetch fails", async () => {
      fetch.mockRejectedValueOnce(new Error("Network Error"));

      await expect(UpdateStudentDetails(mockStudentData)).rejects.toThrow(
        "Network Error"
      );
    });
  });

  describe("GetStudentDetails", () => {
    const mockStudentData = {
      firstName: "John",
      lastName: "Doe",
      dob: "2000-01-01",
      email: "john@example.com",
      phone: "1234567890",
      password: "abc123",
    };

    test("should fetch student details successfully", async () => {
      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockStudentData,
      });

      const result = await GetStudentDetails();

      expect(fetch).toHaveBeenCalledWith("http://localhost:8080/api/GetStudent", {
        method: "Get",
        headers: { "Content-Type": "application/json" },
      });

      expect(result).toEqual(mockStudentData);
    });

    test("should throw error if response not ok", async () => {
      fetch.mockResolvedValueOnce({ ok: false });

      await expect(GetStudentDetails()).rejects.toThrow(
        "Failed to fetch Student Details"
      );
    });

    test("should throw error if fetch fails", async () => {
      fetch.mockRejectedValueOnce(new Error("Network Error"));

      await expect(GetStudentDetails()).rejects.toThrow("Network Error");
    });
  });
});
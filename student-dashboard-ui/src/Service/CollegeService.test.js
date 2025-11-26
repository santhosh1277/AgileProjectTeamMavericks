import { getCollegesList, getCollegeById, getCollegeCourses } from "./CollegeService";

// Mock global fetch
global.fetch = jest.fn();

describe("CollegeService", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    console.error = jest.fn();
  });

  describe("getCollegesList", () => {
    test("fetches colleges successfully", async () => {
      const mockColleges = [
        { id: 1, name: "College A", location: "Location A", rank: 1 },
        { id: 2, name: "College B", location: "Location B", rank: 2 },
      ];

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockColleges,
      });

      const result = await getCollegesList();

      expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges");
      expect(result).toEqual(mockColleges);
    });

    test("throws error when fetch fails", async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 500,
      });

      await expect(getCollegesList()).rejects.toThrow("Failed to fetch colleges: 500");
      expect(console.error).toHaveBeenCalled();
    });

    test("handles network error", async () => {
      const networkError = new Error("Network failure");
      global.fetch.mockRejectedValueOnce(networkError);

      await expect(getCollegesList()).rejects.toThrow("Network failure");
      expect(console.error).toHaveBeenCalledWith("Error fetching colleges:", networkError);
    });
  });

  describe("getCollegeById", () => {
    test("fetches college by ID successfully", async () => {
      const mockCollege = {
        id: 1,
        name: "College A",
        location: "Location A",
        rank: 1,
      };

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockCollege,
      });

      const result = await getCollegeById(1);

      expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges/1");
      expect(result).toEqual(mockCollege);
    });

    test("throws error when college not found", async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 404,
      });

      await expect(getCollegeById(999)).rejects.toThrow("Failed to fetch college: 404");
      expect(console.error).toHaveBeenCalledWith(
        "Error fetching college 999:",
        expect.any(Error)
      );
    });

    test("handles network error for specific college", async () => {
      const networkError = new Error("Connection timeout");
      global.fetch.mockRejectedValueOnce(networkError);

      await expect(getCollegeById(1)).rejects.toThrow("Connection timeout");
      expect(console.error).toHaveBeenCalledWith(
        "Error fetching college 1:",
        networkError
      );
    });
  });

  describe("getCollegeCourses", () => {
    test("fetches courses for college successfully", async () => {
      const mockCourses = [
        { id: 1, name: "Computer Science", duration: "4 years" },
        { id: 2, name: "Engineering", duration: "4 years" },
      ];

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockCourses,
      });

      const result = await getCollegeCourses(1);

      expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges/1/courses");
      expect(result).toEqual(mockCourses);
    });

    test("throws error when courses fetch fails", async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 500,
      });

      await expect(getCollegeCourses(1)).rejects.toThrow("Failed to fetch courses: 500");
      expect(console.error).toHaveBeenCalledWith(
        "Error fetching courses for college 1:",
        expect.any(Error)
      );
    });

    test("handles empty courses list", async () => {
      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      });

      const result = await getCollegeCourses(1);

      expect(result).toEqual([]);
      expect(result.length).toBe(0);
    });

    test("handles network error when fetching courses", async () => {
      const networkError = new Error("Service unavailable");
      global.fetch.mockRejectedValueOnce(networkError);

      await expect(getCollegeCourses(1)).rejects.toThrow("Service unavailable");
      expect(console.error).toHaveBeenCalledWith(
        "Error fetching courses for college 1:",
        networkError
      );
    });
  });

  describe("API Base URL", () => {
    test("uses correct API base URL for all endpoints", async () => {
      global.fetch.mockResolvedValue({
        ok: true,
        json: async () => ({}),
      });

      await getCollegesList();
      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining("http://localhost:8080/api/colleges")
      );

      await getCollegeById(1);
      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining("http://localhost:8080/api/colleges/1")
      );

      await getCollegeCourses(1);
      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining("http://localhost:8080/api/colleges/1/courses")
      );
    });
  });
});

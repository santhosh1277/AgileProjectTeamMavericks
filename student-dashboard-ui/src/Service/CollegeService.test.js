import { getCollegesList, getCollegeById, getCollegeCourses } from "./CollegeService";

// Mock global fetch
globalThis.fetch = jest.fn();

describe("CollegeService", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe("getCollegesList", () => {
    test("fetches colleges successfully", async () => {
      const mockColleges = [
        { id: 1, name: "College A", location: "Location A", rank: 1 },
        { id: 2, name: "College B", location: "Location B", rank: 2 },
      ];

      globalThis.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockColleges,
      });

      const result = await getCollegesList();

      expect(globalThis.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges");
      expect(result).toEqual(mockColleges);
    });

    test("throws error when fetch fails", async () => {
      globalThis.fetch.mockResolvedValueOnce({
        ok: false,
        status: 500,
      });

      await expect(getCollegesList()).rejects.toThrow("Failed to fetch colleges: 500");
    });

    test("handles network error", async () => {
      const networkError = new Error("Network failure");
      globalThis.fetch.mockRejectedValueOnce(networkError);

      await expect(getCollegesList()).rejects.toThrow("Network failure");
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

      globalThis.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockCollege,
      });

      const result = await getCollegeById(1);

      expect(globalThis.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges/1");
      expect(result).toEqual(mockCollege);
    });

    test("throws error when college not found", async () => {
      globalThis.fetch.mockResolvedValueOnce({
        ok: false,
        status: 404,
      });

      await expect(getCollegeById(1)).rejects.toThrow("Failed to fetch college: 404");
    });

    test("handles network error for specific college", async () => {
      const networkError = new Error("Connection timeout");
      globalThis.fetch.mockRejectedValueOnce(networkError);

      await expect(getCollegeById(1)).rejects.toThrow("Connection timeout");
    });
  });

  describe("getCollegeCourses", () => {
    test("fetches courses for college successfully", async () => {
      const mockCourses = [
        { id: 1, name: "Computer Science", duration: "4 years" },
        { id: 2, name: "Engineering", duration: "4 years" },
      ];

      globalThis.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockCourses,
      });

      const result = await getCollegeCourses(1);

      expect(globalThis.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges/1/courses");
      expect(result).toEqual(mockCourses);
    });

    test("throws error when courses fetch fails", async () => {
      globalThis.fetch.mockResolvedValueOnce({
        ok: false,
        status: 500,
      });

      await expect(getCollegeCourses(1)).rejects.toThrow("Failed to fetch courses: 500");
    });

    test("handles empty courses list", async () => {
      globalThis.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      });

      const result = await getCollegeCourses(1);

      expect(result).toEqual([]);
      expect(result.length).toBe(0);
    });

    test("handles network error when fetching courses", async () => {
      const networkError = new Error("Service unavailable");
      globalThis.fetch.mockRejectedValueOnce(networkError);

      await expect(getCollegeCourses(1)).rejects.toThrow("Service unavailable");
    });
  });

  describe("API Base URL", () => {
    test("uses correct API base URL for all endpoints", async () => {
      globalThis.fetch.mockResolvedValue({
        ok: true,
        json: async () => ({}),
      });

      await getCollegesList();
      expect(globalThis.fetch).toHaveBeenCalledWith(
        expect.stringContaining("http://localhost:8080/api/colleges")
      );

      await getCollegeById(1);
      expect(globalThis.fetch).toHaveBeenCalledWith(
        expect.stringContaining("http://localhost:8080/api/colleges/1")
      );

      await getCollegeCourses(1);
      expect(globalThis.fetch).toHaveBeenCalledWith(
        expect.stringContaining("http://localhost:8080/api/colleges/1/courses")
      );
    });
  });
});

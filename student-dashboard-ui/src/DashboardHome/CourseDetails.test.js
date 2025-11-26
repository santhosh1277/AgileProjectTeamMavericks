import { render, screen, waitFor } from "@testing-library/react";
import CourseDetails from "./CourseDetails";

// Mock react-router-dom
const mockNavigate = jest.fn();
const mockUseParams = jest.fn();

jest.mock(
  "react-router-dom",
  () => ({
    useNavigate: () => mockNavigate,
    useParams: () => mockUseParams(),
    BrowserRouter: ({ children }) => children,
    MemoryRouter: ({ children }) => children,
    Route: ({ element }) => element,
    Routes: ({ children }) => children,
  }),
  { virtual: true }
);

// Mock fetch globally
globalThis.fetch = jest.fn();

describe("CourseDetails Component", () => {
  const mockCollegeData = [
    {
      id: 1,
      name: "Trinity College Dublin",
      location: "Dublin, Ireland",
      rank: 101,
      courses: [
        {
          id: 1,
          name: "Computer Science",
          description: "Study of computation and information processing",
        },
        {
          id: 2,
          name: "Business Analytics",
          description: "Data-driven decision making for businesses",
        },
      ],
    },
    {
      id: 2,
      name: "University College Cork",
      location: "Cork, Ireland",
      rank: 250,
      courses: [
        {
          id: 3,
          name: "Engineering",
          description: "Applied sciences and mathematics",
        },
      ],
    },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    mockUseParams.mockReturnValue({ collegeId: "1" });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test("displays loading initially", () => {
    fetch.mockImplementation(
      () =>
        new Promise(() => {}) // Never resolves
    );

    render(<CourseDetails />);

    expect(screen.getByText(/loading course details/i)).toBeInTheDocument();
  });

  test("fetches and displays college details with courses", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockCollegeData,
    });

    render(<CourseDetails />);

    // Wait for college name to appear (using getAllByText because it appears multiple times)
    await waitFor(() => {
      const elements = screen.getAllByText("Trinity College Dublin");
      expect(elements.length).toBeGreaterThan(0);
    });

    // Check location and rank (using getAllByText for duplicate text)
    const locationElements = screen.getAllByText(/Dublin, Ireland/);
    expect(locationElements.length).toBeGreaterThan(0);
    expect(screen.getByText(/World Rank: 101/)).toBeInTheDocument();

    // Check courses are displayed
    expect(screen.getByText("Computer Science")).toBeInTheDocument();
    expect(screen.getByText("Business Analytics")).toBeInTheDocument();
    expect(
      screen.getByText("Study of computation and information processing")
    ).toBeInTheDocument();
  });

  test("displays error when college is not found", async () => {
    mockUseParams.mockReturnValue({ collegeId: "999" });
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockCollegeData,
    });

    render(<CourseDetails />);

    await waitFor(() => {
      expect(screen.getByText("College not found")).toBeInTheDocument();
    });
  });

  test("displays error when fetch fails", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    render(<CourseDetails />);

    await waitFor(() => {
      expect(screen.getByText(/Network error/i)).toBeInTheDocument();
    });

    expect(screen.getByText("Back to Dashboard")).toBeInTheDocument();
  });

  test("displays message when college has no courses", async () => {
    mockUseParams.mockReturnValue({ collegeId: "3" });
    const collegeWithNoCourses = [
      {
        id: 3,
        name: "Sample University",
        location: "Dublin, Ireland",
        rank: 300,
        courses: [],
      },
    ];

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => collegeWithNoCourses,
    });

    render(<CourseDetails />);

    await waitFor(() => {
      const elements = screen.getAllByText("Sample University");
      expect(elements.length).toBeGreaterThan(0);
    });

    expect(screen.getByText(/No Courses Available/i)).toBeInTheDocument();
    expect(
      screen.getByText(/This college currently has no courses listed/i)
    ).toBeInTheDocument();
  });

  test("displays correct number of courses", async () => {
    mockUseParams.mockReturnValue({ collegeId: "1" });
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockCollegeData,
    });

    render(<CourseDetails />);

    await waitFor(() => {
      const elements = screen.getAllByText("Trinity College Dublin");
      expect(elements.length).toBeGreaterThan(0);
    });

    // Should display 2 courses
    const learnMoreButtons = screen.getAllByText("Learn More");
    expect(learnMoreButtons).toHaveLength(2);
  });

  test("handles response not ok", async () => {
    mockUseParams.mockReturnValue({ collegeId: "1" });
    fetch.mockResolvedValueOnce({
      ok: false,
    });

    render(<CourseDetails />);

    await waitFor(() => {
      expect(
        screen.getByText(/Failed to fetch college details/i)
      ).toBeInTheDocument();
    });
  });
});

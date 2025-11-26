const API_BASE_URL = "http://localhost:8080/api";

/**
 * Fetches all colleges from the API
 * @returns {Promise<Array>} List of colleges
 */
export const getCollegesList = async () => {
  const response = await fetch(`${API_BASE_URL}/colleges`);
  
  if (!response.ok) {
    throw new Error(`Failed to fetch colleges: ${response.status}`);
  }
  
  const data = await response.json();
  return data;
};

/**
 * Fetches a single college by ID
 * @param {number} collegeId - The ID of the college
 * @returns {Promise<Object>} College details
 */
export const getCollegeById = async (collegeId) => {
  const response = await fetch(`${API_BASE_URL}/colleges/${collegeId}`);
  
  if (!response.ok) {
    throw new Error(`Failed to fetch college: ${response.status}`);
  }
  
  const data = await response.json();
  return data;
};

/**
 * Fetches courses for a specific college
 * @param {number} collegeId - The ID of the college
 * @returns {Promise<Array>} List of courses
 */
export const getCollegeCourses = async (collegeId) => {
  const response = await fetch(`${API_BASE_URL}/colleges/${collegeId}/courses`);
  
  if (!response.ok) {
    throw new Error(`Failed to fetch courses: ${response.status}`);
  }
  
  const data = await response.json();
  return data;
};

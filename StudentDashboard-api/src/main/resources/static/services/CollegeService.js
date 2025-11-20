const apiBaseUrl = "http://localhost:8080/api";

export const getCollegesList = async () => {
  try {
    const response = await fetch(`${apiBaseUrl}/colleges`);
    if (!response.ok) {
      throw new Error("Failed to fetch colleges");
    }
    const data = await response.json();
    console.log("Fetched colleges data:", data);
    return data;
  } catch (error) {
    console.error("Error in getCollegesList:", error);
    throw error;
  }
};


const apiBaseUrl= "http://localhost:8080/api";

export const getCollegesList = async () => {
  try {
    const response = await fetch(apiBaseUrl + "/colleges");
    return response.data;
    } catch (error) {
    throw error;
    }
};
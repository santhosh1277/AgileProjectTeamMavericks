const apiBaseUrl= "http://localhost:8080/api";

export const getCollegesList = async () => {
  try {
    const response = await fetch(apiBaseUrl + "/colleges");
    const data = await response.json(); 
    console.log("Fetched colleges data:", data);
    return data;
    } catch (error) {
    throw error;
    }
};
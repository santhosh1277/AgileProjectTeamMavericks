import axios from "axios";

const apiBaseUrl = process.env.REACT_APP_API_URL || "/";

const apiClient = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      return Promise.reject({
        status: error.response.status,
        data: error.response.data,
        message: error.response.data?.message || error.message,
      });
    }
    return Promise.reject(error);
  }
);

export default apiClient;




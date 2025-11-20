const apiBaseUrl = "http://localhost:8080/api";

export const UpdateStudentDetails = async (user) => {
    try {
        const response = await fetch(`${apiBaseUrl}/Student`, {
            method: 'Post',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(user),

        });
        if (!response.ok) {
            throw new Error("Failed to Update Student Details");
        }
        const data = await response.json();
        console.log("Updated Student data:", data);
        return data;
    } catch (error) {
        console.error("Error in UpdateStudentDetails:", error);
        throw error;
    }
};

export  const GetStudentDetails = async () => {
    try {
        const response = await fetch(`${apiBaseUrl}/GetStudent`, {
            method: 'Get',
            headers: {
                "Content-Type": "application/json",
            },
        });         
        if (!response.ok) {
            throw new Error("Failed to fetch Student Details");
        }
        const data = await response.json();
        console.log("Fetched Student data:", data);
        return data;
    }
    catch (error) {
        console.error("Error in GetStudentDetails:", error);
        throw error;
    }
};  

const apiBaseUrl = "http://localhost:8080/api/students";

export const UpdateStudentDetails = async (user) => {
    try {
        const response = await fetch(`${apiBaseUrl}/update`, {
            method: 'Put',
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

export const GetStudentDetails = async (email) => {
    try {
        const response = await fetch(`${apiBaseUrl}/profile`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email }), 
            
        });
        console.log("Request sent with email:", email);

        if (!response.ok) {
            throw new Error("Failed to fetch Student Details");
        }

        const data = await response.json();
        console.log("Fetched Student data:", data);

        return data;  // ⬅️ this will be entire student object
    } catch (error) {
        console.error("Error in GetStudentDetails:", error);
        throw error;
    }
};
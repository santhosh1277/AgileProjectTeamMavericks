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
        return data;
    } catch (error) {
        throw new Error(`Failed to update student details: ${error.message}`);
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

        if (!response.ok) {
            throw new Error("Failed to fetch Student Details");
        }

        const data = await response.json();

        return data;  // ⬅️ this will be entire student object
    } catch (error) {
        throw new Error(`Failed to fetch student details: ${error.message}`);
    }
};
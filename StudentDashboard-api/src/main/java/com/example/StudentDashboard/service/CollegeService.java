package com.example.StudentDashboard.service;
import org.springframework.stereotype.Service;
import com.example.StudentDashboard.model.College;
import java.util.List;
@Service
public class CollegeService {

	
	    public List<College> getAllColleges() {
	        return List.of(
	            new College(1, "Athlone Institute of Technology",
	                    List.of("Computer Science", "Engineering", "Business"),
	                    "Athlone, Ireland", 850),
	            new College(2, "Trinity College Dublin",
	                    List.of("Medicine", "Law", "Economics"),
	                    "Dublin, Ireland", 98)
	        );
	    }
}
	   

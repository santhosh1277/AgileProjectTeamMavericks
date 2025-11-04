package com.example.StudentDashboard.service;
import org.springframework.stereotype.Service;
import com.example.StudentDashboard.model.College;
import com.example.StudentDashboard.repository.CollegeRepository;

import java.util.List;
@Service
public class CollegeService {

	private CollegeRepository _collegeRepository;
	public CollegeService(CollegeRepository collegeRepository)
	{
		_collegeRepository = collegeRepository;
	}
	    public List<com.example.StudentDashboard.Entity.College> getAllColleges() {
	    	        return _collegeRepository.findAll();
	    	    }
	    }
	   

package com.example.StudentDashboard.service;
import org.springframework.stereotype.Service;
import com.example.StudentDashboard.repository.CollegeRepository;
import com.example.StudentDashboard.entity.College;
import com.example.StudentDashboard.entity.CourseEntity;
import com.example.StudentDashboard.model.UniversityApiResponse;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Service
public class CollegeService {

	private CollegeRepository _collegeRepository;
	private final RestTemplate _restTemplate;
	public CollegeService(CollegeRepository collegeRepository,RestTemplate restTemplate)
	{
		_collegeRepository = collegeRepository;
		_restTemplate = restTemplate;
	}
	    public List<College> getAllColleges() {
	    	        return _collegeRepository.findAll();
	    	    }
	    public College AddPreferenceCollege()
	    {
	    	return new College(null, null, null, null, null, null, null);
	    }
	    public void UpdateCollegesList() {
	        List<String> topCountries = List.of(
	                "United Kingdom",
	                "Ireland",
	                "Singapore",
	                "Sweden",
	                "Switzerland",
	                "Netherlands",
	                "Australia",
	                "Canada",
	                "Germany",
	                "India"
	        );

	        List<String> courseNames = List.of(
	                "MSc Applied Software Engineering",
	                "MSc Software Engineering",
	                "MSc Data Analytics",
	                "MSc Software Design with Cloud Native Computing",
	                "MSc Software Design with Cybersecurity",
	                "MSc Software Design with Artificial Intelligence",
	                "MSc Biopharmaceutical Technology",
	                "MSc Pharmaceutical & Chemical Analysis",
	                "MSc Digital Health",
	                "MSc Digital Marketing",
	                "MA Accounting",
	                "Master of Business (General Business Master)",
	                "MEng Engineering Management"
	        );

	        for (String country : topCountries) {
	            try {
	                String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);
	                String url = "http://universities.hipolabs.com/search?country=" + encodedCountry;

	                UniversityApiResponse[] response = _restTemplate.getForObject(url, UniversityApiResponse[].class);

	                if (response != null) {
	                    for (UniversityApiResponse uni : response) {
	                        if (uni == null) continue;

	                        // Delete existing college if present
	                        _collegeRepository.findByNameAndCountry(uni.getName(), uni.getCountry())
	                                .ifPresent(_collegeRepository::delete);

	                        // Create new college
	                        College college = new College();
	                        college.setName(uni.getName());
	                        college.setCountry(uni.getCountry());
	                        college.setStateProvince(uni.getState_province());
	                        college.setAlphaTwoCode(uni.getAlpha_two_code());
	                        college.setDomains(uni.getDomains() != null ? String.join(",", uni.getDomains()) : null);
	                        college.setWebPages(uni.getWeb_pages());

	                        // Add courses
	                        List<CourseEntity> courses = courseNames.stream().map(courseName -> {
	                            CourseEntity course = new CourseEntity();
	                            course.setName(courseName);
	                            course.setCollege(college);
	                            return course;
	                        }).toList();
	                        college.setCourses(courses);

	                        // Save college with courses
	                        _collegeRepository.save(college);
	                    }
	                }

	            } catch (Exception e) {
	                System.err.println("Failed to fetch colleges for country: " + country + ", error: " + e.getMessage());
	            }
	        }
	    }



}

	   

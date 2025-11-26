package com.example.StudentDashboard.service;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.Entity.CourseEntity;
import com.example.StudentDashboard.model.UniversityApiResponse;
import com.example.StudentDashboard.repository.CollegeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CollegeServiceTest {

    @InjectMocks
    private CollegeService collegeService;

    @Mock
    private CollegeRepository collegeRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateCollegesList() throws Exception {
        // Mock API response
        UniversityApiResponse mockUniversity = new UniversityApiResponse();
        mockUniversity.setName("Test University");
        mockUniversity.setCountry("United Kingdom");
        mockUniversity.setState_province("Test Province");
        mockUniversity.setAlpha_two_code("GB");
        mockUniversity.setDomains(List.of("test.ac.uk"));
        mockUniversity.setWeb_pages(List.of("https://test.ac.uk"));

        UniversityApiResponse[] apiResponse = new UniversityApiResponse[]{mockUniversity};

        // Mock RestTemplate call
        when(restTemplate.getForObject(anyString(), eq(UniversityApiResponse[].class)))
                .thenReturn(apiResponse);

        // Mock repository find (no existing college)
        when(collegeRepository.findByNameAndCountry(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Call method
        collegeService.UpdateCollegesList();

        // Verify that collegeRepository.save was called
        ArgumentCaptor<College> captor = ArgumentCaptor.forClass(College.class);
        verify(collegeRepository, atLeastOnce()).save(captor.capture());

        College savedCollege = captor.getValue();
        assertEquals("Test University", savedCollege.getName());
        assertEquals("United Kingdom", savedCollege.getCountry());

        // Verify courses are added
        List<CourseEntity> courses = savedCollege.getCourses();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
        assertEquals(13, courses.size()); // 13 courses in your list
        assertEquals("MSc Applied Software Engineering", courses.get(0).getName());
        assertEquals(savedCollege, courses.get(0).getCollege());
    }
    @Test
    void testAddPreferenceCollege() {
        // Call the method
        College college = collegeService.AddPreferenceCollege();

        // Assertions
        assertNotNull(college, "The college object should not be null");
        assertNull(college.getId(), "Id should be null");
        assertNull(college.getName(), "Name should be null");
        assertNull(college.getCountry(), "Country should be null");
        assertNull(college.getStateProvince(), "StateProvince should be null");
        assertNull(college.getAlphaTwoCode(), "AlphaTwoCode should be null");
        assertNull(college.getDomains(), "Domains should be null");
        assertNull(college.getWebPages(), "WebPages should be null");
    }

}

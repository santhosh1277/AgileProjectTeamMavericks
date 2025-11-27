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
    void testGetAllColleges() {
        College college1 = new College();
        college1.setId(1L);
        college1.setName("University 1");
        college1.setCountry("Ireland");

        College college2 = new College();
        college2.setId(2L);
        college2.setName("University 2");
        college2.setCountry("UK");

        List<College> colleges = List.of(college1, college2);
        
        when(collegeRepository.findAll()).thenReturn(colleges);

        List<College> result = collegeService.getAllColleges();

        assertEquals(2, result.size());
        assertEquals("University 1", result.get(0).getName());
        assertEquals("University 2", result.get(1).getName());
        verify(collegeRepository).findAll();
    }

    @Test
    void testGetAllCollegesEmpty() {
        when(collegeRepository.findAll()).thenReturn(List.of());

        List<College> result = collegeService.getAllColleges();

        assertTrue(result.isEmpty());
        verify(collegeRepository).findAll();
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
    void testUpdateCollegesListWithExistingCollege() {
        // Mock API response with specific country
        UniversityApiResponse mockUniversity = new UniversityApiResponse();
        mockUniversity.setName("Existing University");
        mockUniversity.setCountry("Ireland");
        mockUniversity.setState_province("Dublin");
        mockUniversity.setAlpha_two_code("IE");
        mockUniversity.setDomains(List.of("existing.ie"));
        mockUniversity.setWeb_pages(List.of("https://existing.ie"));

        UniversityApiResponse[] apiResponse = new UniversityApiResponse[]{mockUniversity};

        College existingCollege = new College();
        existingCollege.setId(1L);
        existingCollege.setName("Existing University");
        existingCollege.setCountry("Ireland");

        // Mock RestTemplate to return API response for any country
        when(restTemplate.getForObject(anyString(), eq(UniversityApiResponse[].class)))
                .thenReturn(apiResponse);

        // Mock repository to return existing college for "Existing University" and "Ireland"
        when(collegeRepository.findByNameAndCountry("Existing University", "Ireland"))
                .thenReturn(Optional.of(existingCollege));

        collegeService.UpdateCollegesList();

        // Verify that the existing college was deleted at least once
        verify(collegeRepository, atLeastOnce()).delete(any(College.class));
        
        // Verify that a save was called for a new college (replacement)
        verify(collegeRepository, atLeastOnce()).save(any(College.class));
    }

    @Test
    void testUpdateCollegesListNullResponse() {
        when(restTemplate.getForObject(anyString(), eq(UniversityApiResponse[].class)))
                .thenReturn(null);

        // Should not throw exception, just continue
        assertDoesNotThrow(() -> collegeService.UpdateCollegesList());
    }

    @Test
    void testUpdateCollegesListWithNullUniversity() {
        UniversityApiResponse nullUniversity = null;
        UniversityApiResponse validUniversity = new UniversityApiResponse();
        validUniversity.setName("Valid University");
        validUniversity.setCountry("UK");
        validUniversity.setState_province("London");
        validUniversity.setAlpha_two_code("GB");
        validUniversity.setDomains(List.of("valid.ac.uk"));
        validUniversity.setWeb_pages(List.of("https://valid.ac.uk"));

        UniversityApiResponse[] apiResponse = new UniversityApiResponse[]{nullUniversity, validUniversity};

        when(restTemplate.getForObject(anyString(), eq(UniversityApiResponse[].class)))
                .thenReturn(apiResponse);

        when(collegeRepository.findByNameAndCountry(anyString(), anyString()))
                .thenReturn(Optional.empty());

        collegeService.UpdateCollegesList();

        // Verify that at least one save occurred (for valid university)
        verify(collegeRepository, atLeastOnce()).save(any(College.class));
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

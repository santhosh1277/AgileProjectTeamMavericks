package com.example.StudentDashboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.Entity.CourseEntity;
import com.example.StudentDashboard.service.CollegeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CollegeController.class)
class CollegeControllerTestNew {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollegeService collegeService;

    @Autowired
    private ObjectMapper objectMapper;

    private College testCollege;

    @BeforeEach
    void setUp() {
        testCollege = new College();
        testCollege.setId(1L);
        testCollege.setName("MIT");
        testCollege.setCountry("USA");
        testCollege.setStateProvince("Massachusetts");
        testCollege.setAlphaTwoCode("US");
        testCollege.setDomains("mit.edu");
        testCollege.setWebPages(new ArrayList<>());
        testCollege.setCourses(new ArrayList<>());
    }

    @Test
    void testGetAllColleges() throws Exception {
        List<College> colleges = new ArrayList<>();
        colleges.add(testCollege);

        when(collegeService.getAllColleges()).thenReturn(colleges);

        mockMvc.perform(get("/api/colleges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("MIT"))
                .andExpect(jsonPath("$[0].country").value("USA"));

        verify(collegeService, times(1)).getAllColleges();
    }

    @Test
    void testGetAllCollegesEmpty() throws Exception {
        when(collegeService.getAllColleges()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/colleges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(collegeService, times(1)).getAllColleges();
    }

    @Test
    void testAddPreferenceCollege() throws Exception {
        when(collegeService.AddPreferenceCollege()).thenReturn(testCollege);

        mockMvc.perform(post("/api/colleges/preference"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MIT"));

        verify(collegeService, times(1)).AddPreferenceCollege();
    }

    @Test
    void testUpdateCollegesList() throws Exception {
        doNothing().when(collegeService).UpdateCollegesList();

        mockMvc.perform(post("/api/colleges/update"))
                .andExpect(status().isOk());

        verify(collegeService, times(1)).UpdateCollegesList();
    }

    @Test
    void testUpdateCollegesListException() throws Exception {
        doThrow(new RuntimeException("API Error")).when(collegeService).UpdateCollegesList();

        mockMvc.perform(post("/api/colleges/update"))
                .andExpect(status().isInternalServerError());

        verify(collegeService, times(1)).UpdateCollegesList();
    }

    @Test
    void testGetAllCollegesMultiple() throws Exception {
        College college2 = new College();
        college2.setId(2L);
        college2.setName("Stanford");
        college2.setCountry("USA");

        List<College> colleges = new ArrayList<>();
        colleges.add(testCollege);
        colleges.add(college2);

        when(collegeService.getAllColleges()).thenReturn(colleges);

        mockMvc.perform(get("/api/colleges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("MIT"))
                .andExpect(jsonPath("$[1].name").value("Stanford"));
    }

    @Test
    void testCollegeWithCourses() throws Exception {
        CourseEntity course = new CourseEntity();
        course.setId(1L);
        course.setName("CS101");
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(course);
        testCollege.setCourses(courses);

        List<College> colleges = new ArrayList<>();
        colleges.add(testCollege);

        when(collegeService.getAllColleges()).thenReturn(colleges);

        mockMvc.perform(get("/api/colleges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courses[0].name").value("CS101"));
    }
}

package com.example.StudentDashboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.Entity.CourseEntity;
import com.example.StudentDashboard.repository.CourseRepository;
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
@WebMvcTest(CourseController.class)
class CourseControllerTestNew {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseEntity testCourse;
    private College testCollege;

    @BeforeEach
    void setUp() {
        testCollege = new College();
        testCollege.setId(1L);
        testCollege.setName("MIT");

        testCourse = new CourseEntity();
        testCourse.setId(1L);
        testCourse.setName("CS101");
        testCourse.setCollege(testCollege);
    }

    @Test
    void testGetAllCourses() throws Exception {
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(testCourse);

        when(courseRepository.findAll()).thenReturn(courses);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("CS101"))
                .andExpect(jsonPath("$[0].college.name").value("MIT"));

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCoursesEmpty() throws Exception {
        when(courseRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetMastersCourses() throws Exception {
        CourseEntity mastersCourse = new CourseEntity();
        mastersCourse.setId(1L);
        mastersCourse.setName("CS501");
        mastersCourse.setCollege(testCollege);

        List<CourseEntity> allCourses = new ArrayList<>();
        allCourses.add(mastersCourse);

        when(courseRepository.findAll()).thenReturn(allCourses);

        mockMvc.perform(get("/api/courses/masters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetMastersCoursesMultiple() throws Exception {
        CourseEntity mastersCourse1 = new CourseEntity();
        mastersCourse1.setId(1L);
        mastersCourse1.setName("CS501");
        mastersCourse1.setCollege(testCollege);

        CourseEntity mastersCourse2 = new CourseEntity();
        mastersCourse2.setId(2L);
        mastersCourse2.setName("CS502");
        mastersCourse2.setCollege(testCollege);

        CourseEntity bachelorsCourse = new CourseEntity();
        bachelorsCourse.setId(3L);
        bachelorsCourse.setName("CS101");
        bachelorsCourse.setCollege(testCollege);

        List<CourseEntity> allCourses = new ArrayList<>();
        allCourses.add(mastersCourse1);
        allCourses.add(mastersCourse2);
        allCourses.add(bachelorsCourse);

        when(courseRepository.findAll()).thenReturn(allCourses);

        mockMvc.perform(get("/api/courses/masters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCoursesMultiple() throws Exception {
        CourseEntity course2 = new CourseEntity();
        course2.setId(2L);
        course2.setName("CS102");
        course2.setCollege(testCollege);

        List<CourseEntity> courses = new ArrayList<>();
        courses.add(testCourse);
        courses.add(course2);

        when(courseRepository.findAll()).thenReturn(courses);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("CS101"))
                .andExpect(jsonPath("$[1].name").value("CS102"));
    }

    @Test
    void testCourseWithCompleteDetails() throws Exception {
        testCourse.setCollege(testCollege);
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(testCourse);

        when(courseRepository.findAll()).thenReturn(courses);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("CS101"))
                .andExpect(jsonPath("$[0].college.id").value(1))
                .andExpect(jsonPath("$[0].college.name").value("MIT"));
    }
}

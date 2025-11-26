package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.service.CollegeService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CollegeController.class)
class CollegeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollegeService collegeService;

    @Test
    void testUpdateCollegesListEndpoint() throws Exception {
        mockMvc.perform(get("/api/colleges/updatecolleges"))
                .andExpect(status().isOk());

        // Verify that the service method is called
        verify(collegeService, Mockito.times(1)).UpdateCollegesList();
    }
    @Test
    void testGetAllCollegesEndpoint() throws Exception {
        // Sample data
        College college1 = new College();
        college1.setId(1L);
        college1.setName("Trinity College Dublin");

        College college2 = new College();
        college2.setId(2L);
        college2.setName("University College Dublin");

        List<College> colleges = List.of(college1, college2);

        // Mock the service
        Mockito.when(collegeService.getAllColleges()).thenReturn(colleges);

        // Perform GET request
        mockMvc.perform(get("/api/colleges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(colleges.size()))
                .andExpect(jsonPath("$[0].name").value("Trinity College Dublin"))
                .andExpect(jsonPath("$[1].name").value("University College Dublin"));

        // Verify service call
        verify(collegeService, Mockito.times(1)).getAllColleges();
    }
}

package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.repository.CollegeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CollegeController.class)
class CollegeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CollegeRepository collegeRepository;

	@Test
	void getAllColleges_returnsList() throws Exception {
		College c = new College();
		c.setId(1L);
		c.setName("Test College");
		c.setLocation("Dublin");
		c.setRank(100);

		Mockito.when(collegeRepository.findAll()).thenReturn(List.of(c));

		mockMvc.perform(get("/api/colleges").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Test College"))
				.andExpect(jsonPath("$[0].location").value("Dublin"))
				.andExpect(jsonPath("$[0].rank").value(100));
	}
}

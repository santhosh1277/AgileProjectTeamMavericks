package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.model.UniversityApiResponse;
import com.example.StudentDashboard.service.UniversityApiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UniversityApiController.class)
class UniversityApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UniversityApiService universityApiService;

	@Test
	void returnsUniversitiesForCountry() throws Exception {
		UniversityApiResponse uni = new UniversityApiResponse();
		uni.setName("Test University");
		uni.setCountry("Ireland");

		Mockito.when(universityApiService.getUniversitiesByCountry(eq("Ireland")))
				.thenReturn(List.of(uni));

		mockMvc.perform(get("/api/universities").param("country", "Ireland")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value("Test University"))
				.andExpect(jsonPath("$[0].country").value("Ireland"));
	}
}

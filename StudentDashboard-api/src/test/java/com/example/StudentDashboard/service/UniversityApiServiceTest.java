package com.example.StudentDashboard.service;

import com.example.StudentDashboard.model.UniversityApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class UniversityApiServiceTest {

	@Test
	void returnsListFromExternalApiViaMock() {
		// Arrange: mock RestTemplate and inject into service
		RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
		UniversityApiResponse mockResp = new UniversityApiResponse();
		mockResp.setName("Mocked University");
		mockResp.setCountry("Ireland");

		Mockito.when(mockTemplate.getForObject(Mockito.contains("country=Ireland"), Mockito.eq(UniversityApiResponse[].class)))
				.thenReturn(new UniversityApiResponse[]{mockResp});

		UniversityApiService service = new UniversityApiService();
		// Replace private final field for test purposes
		ReflectionTestUtils.setField(service, "restTemplate", mockTemplate);

		// Act
		var list = service.getUniversitiesByCountry("Ireland");

		// Assert
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("Mocked University", list.get(0).getName());
		assertEquals("Ireland", list.get(0).getCountry());
	}
}

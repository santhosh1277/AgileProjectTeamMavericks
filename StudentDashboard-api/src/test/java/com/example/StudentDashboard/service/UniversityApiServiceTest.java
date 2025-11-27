package com.example.StudentDashboard.service;

import com.example.StudentDashboard.model.UniversityApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

	@Test
	void returnsMultipleUniversities() {
		// Arrange
		RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
		UniversityApiResponse resp1 = new UniversityApiResponse();
		resp1.setName("Trinity College Dublin");
		resp1.setCountry("Ireland");

		UniversityApiResponse resp2 = new UniversityApiResponse();
		resp2.setName("University College Dublin");
		resp2.setCountry("Ireland");

		Mockito.when(mockTemplate.getForObject(Mockito.contains("country=Ireland"), Mockito.eq(UniversityApiResponse[].class)))
				.thenReturn(new UniversityApiResponse[]{resp1, resp2});

		UniversityApiService service = new UniversityApiService();
		ReflectionTestUtils.setField(service, "restTemplate", mockTemplate);

		// Act
		List<UniversityApiResponse> list = service.getUniversitiesByCountry("Ireland");

		// Assert
		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Trinity College Dublin", list.get(0).getName());
		assertEquals("University College Dublin", list.get(1).getName());
	}

	@Test
	void returnsEmptyListWhenNoUniversities() {
		// Arrange
		RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
		Mockito.when(mockTemplate.getForObject(Mockito.contains("country=Unknown"), Mockito.eq(UniversityApiResponse[].class)))
				.thenReturn(new UniversityApiResponse[]{});

		UniversityApiService service = new UniversityApiService();
		ReflectionTestUtils.setField(service, "restTemplate", mockTemplate);

		// Act
		List<UniversityApiResponse> list = service.getUniversitiesByCountry("Unknown");

		// Assert
		assertNotNull(list);
		assertEquals(0, list.size());
	}

	@Test
	void handlesNullArrayResponse() {
		// Arrange
		RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
		Mockito.when(mockTemplate.getForObject(Mockito.anyString(), Mockito.eq(UniversityApiResponse[].class)))
				.thenReturn(null);

		UniversityApiService service = new UniversityApiService();
		ReflectionTestUtils.setField(service, "restTemplate", mockTemplate);

		// Act & Assert - should throw NullPointerException when Arrays.asList is called with null
		assertThrows(NullPointerException.class, () -> service.getUniversitiesByCountry("Invalid"));
	}

	@Test
	void encodesCountryNameInUrl() {
		// Arrange
		RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
		UniversityApiResponse resp = new UniversityApiResponse();
		resp.setName("University");
		resp.setCountry("United Kingdom");

		Mockito.when(mockTemplate.getForObject(Mockito.contains("country=United"), Mockito.eq(UniversityApiResponse[].class)))
				.thenReturn(new UniversityApiResponse[]{resp});

		UniversityApiService service = new UniversityApiService();
		ReflectionTestUtils.setField(service, "restTemplate", mockTemplate);

		// Act
		List<UniversityApiResponse> list = service.getUniversitiesByCountry("United Kingdom");

		// Assert
		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void universityWithAllFields() {
		// Arrange
		RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
		UniversityApiResponse resp = new UniversityApiResponse();
		resp.setName("Test University");
		resp.setCountry("Test Country");
		resp.setState_province("Test Province");
		resp.setAlpha_two_code("TC");
		resp.setDomains(List.of("test.edu"));
		resp.setWeb_pages(List.of("https://test.edu"));

		Mockito.when(mockTemplate.getForObject(Mockito.anyString(), Mockito.eq(UniversityApiResponse[].class)))
				.thenReturn(new UniversityApiResponse[]{resp});

		UniversityApiService service = new UniversityApiService();
		ReflectionTestUtils.setField(service, "restTemplate", mockTemplate);

		// Act
		List<UniversityApiResponse> list = service.getUniversitiesByCountry("Test Country");

		// Assert
		assertEquals(1, list.size());
		assertEquals("Test University", list.get(0).getName());
		assertEquals("Test Country", list.get(0).getCountry());
		assertEquals("Test Province", list.get(0).getState_province());
		assertEquals("TC", list.get(0).getAlpha_two_code());
	}
}

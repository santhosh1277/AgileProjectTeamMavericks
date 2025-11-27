package com.example.StudentDashboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.StudentDashboard.model.UniversityApiResponse;
import com.example.StudentDashboard.service.UniversityApiService;
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
@WebMvcTest(UniversityApiController.class)
class UniversityApiControllerTestNew {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversityApiService universityApiService;

    @Autowired
    private ObjectMapper objectMapper;

    private UniversityApiResponse testUniversity;

    @BeforeEach
    void setUp() {
        testUniversity = new UniversityApiResponse();
        testUniversity.setName("MIT");
        testUniversity.setCountry("United States");
        testUniversity.setAlpha_two_code("US");
        testUniversity.setWeb_pages(new ArrayList<>());
        testUniversity.setDomains(new ArrayList<>());
        testUniversity.setState_province("Massachusetts");
    }

    @Test
    void testGetUniversitiesByCountry() throws Exception {
        List<UniversityApiResponse> universities = new ArrayList<>();
        universities.add(testUniversity);

        when(universityApiService.getUniversitiesByCountry("United States"))
                .thenReturn(universities);

        mockMvc.perform(get("/api/universities")
                .param("country", "United States"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("MIT"))
                .andExpect(jsonPath("$[0].country").value("United States"))
                .andExpect(jsonPath("$[0].alpha_two_code").value("US"));

        verify(universityApiService, times(1)).getUniversitiesByCountry("United States");
    }

    @Test
    void testGetUniversitiesByCountryEmpty() throws Exception {
        when(universityApiService.getUniversitiesByCountry("Unknown"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/universities")
                .param("country", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(universityApiService, times(1)).getUniversitiesByCountry("Unknown");
    }

    @Test
    void testGetUniversitiesByCountryMultiple() throws Exception {
        UniversityApiResponse uni2 = new UniversityApiResponse();
        uni2.setName("Stanford");
        uni2.setCountry("United States");
        uni2.setAlpha_two_code("US");
        uni2.setWeb_pages(new ArrayList<>());
        uni2.setDomains(new ArrayList<>());

        List<UniversityApiResponse> universities = new ArrayList<>();
        universities.add(testUniversity);
        universities.add(uni2);

        when(universityApiService.getUniversitiesByCountry("United States"))
                .thenReturn(universities);

        mockMvc.perform(get("/api/universities")
                .param("country", "United States"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("MIT"))
                .andExpect(jsonPath("$[1].name").value("Stanford"));

        verify(universityApiService, times(1)).getUniversitiesByCountry("United States");
    }

    @Test
    void testGetUniversitiesWithStateProvince() throws Exception {
        List<UniversityApiResponse> universities = new ArrayList<>();
        universities.add(testUniversity);

        when(universityApiService.getUniversitiesByCountry("United States"))
                .thenReturn(universities);

        mockMvc.perform(get("/api/universities")
                .param("country", "United States"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state_province").value("Massachusetts"));
    }

    @Test
    void testGetUniversitiesCompleteDetails() throws Exception {
        List<String> webPages = new ArrayList<>();
        webPages.add("https://mit.edu");
        List<String> domains = new ArrayList<>();
        domains.add("mit.edu");

        testUniversity.setWeb_pages(webPages);
        testUniversity.setDomains(domains);

        List<UniversityApiResponse> universities = new ArrayList<>();
        universities.add(testUniversity);

        when(universityApiService.getUniversitiesByCountry("United States"))
                .thenReturn(universities);

        mockMvc.perform(get("/api/universities")
                .param("country", "United States"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("MIT"))
                .andExpect(jsonPath("$[0].web_pages[0]").value("https://mit.edu"))
                .andExpect(jsonPath("$[0].domains[0]").value("mit.edu"));
    }

    @Test
    void testGetUniversitiesSpecialCharacters() throws Exception {
        UniversityApiResponse uniSpecial = new UniversityApiResponse();
        uniSpecial.setName("Université de Montréal");
        uniSpecial.setCountry("Canada");
        uniSpecial.setAlpha_two_code("CA");
        uniSpecial.setWeb_pages(new ArrayList<>());
        uniSpecial.setDomains(new ArrayList<>());

        List<UniversityApiResponse> universities = new ArrayList<>();
        universities.add(uniSpecial);

        when(universityApiService.getUniversitiesByCountry("Canada"))
                .thenReturn(universities);

        mockMvc.perform(get("/api/universities")
                .param("country", "Canada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Université de Montréal"))
                .andExpect(jsonPath("$[0].country").value("Canada"));
    }

    @Test
    void testGetUniversitiesNullStateProvince() throws Exception {
        UniversityApiResponse uni = new UniversityApiResponse();
        uni.setName("Foreign University");
        uni.setCountry("Japan");
        uni.setAlpha_two_code("JP");
        uni.setWeb_pages(new ArrayList<>());
        uni.setDomains(new ArrayList<>());
        uni.setState_province(null);

        List<UniversityApiResponse> universities = new ArrayList<>();
        universities.add(uni);

        when(universityApiService.getUniversitiesByCountry("Japan"))
                .thenReturn(universities);

        mockMvc.perform(get("/api/universities")
                .param("country", "Japan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state_province").doesNotExist());
    }
}

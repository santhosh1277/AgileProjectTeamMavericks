package com.example.StudentDashboard.service;

import com.example.StudentDashboard.model.UniversityApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UniversityApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<UniversityApiResponse> getUniversitiesByCountry(String country) {
        String url = "http://universities.hipolabs.com/search?country=" + country;
        UniversityApiResponse[] response =
                restTemplate.getForObject(url, UniversityApiResponse[].class);
        return Arrays.asList(response);
    }
}

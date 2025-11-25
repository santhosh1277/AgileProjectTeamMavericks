package com.example.StudentDashboard.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniversityApiResponseTest {

    @Test
    void testGettersAndSetters() {
        UniversityApiResponse resp = new UniversityApiResponse();

        resp.setName("Trinity College");
        resp.setCountry("Ireland");
        resp.setAlpha_two_code("IE");
        resp.setDomains(Arrays.asList("tcd.ie"));
        resp.setWeb_pages(Arrays.asList("http://tcd.ie"));
        resp.setState_province("Dublin");

        assertEquals("Trinity College", resp.getName());
        assertEquals("Ireland", resp.getCountry());
        assertEquals("IE", resp.getAlpha_two_code());
        assertEquals("tcd.ie", resp.getDomains().get(0));
        assertEquals("http://tcd.ie", resp.getWeb_pages().get(0));
        assertEquals("Dublin", resp.getState_province());
    }
}

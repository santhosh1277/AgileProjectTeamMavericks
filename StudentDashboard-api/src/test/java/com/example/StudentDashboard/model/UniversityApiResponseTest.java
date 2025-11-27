package com.example.StudentDashboard.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testEmptyCollections() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setName("Empty University");
        resp.setCountry("USA");
        resp.setAlpha_two_code("US");
        resp.setDomains(new ArrayList<>());
        resp.setWeb_pages(new ArrayList<>());

        assertTrue(resp.getDomains().isEmpty());
        assertTrue(resp.getWeb_pages().isEmpty());
        assertEquals(0, resp.getDomains().size());
        assertEquals(0, resp.getWeb_pages().size());
    }

    @Test
    void testMultipleDomains() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setName("Multi Domain University");
        resp.setCountry("UK");
        resp.setDomains(Arrays.asList("uni.ac.uk", "uni.co.uk", "mail.uni.ac.uk"));

        assertEquals(3, resp.getDomains().size());
        assertTrue(resp.getDomains().contains("uni.ac.uk"));
        assertTrue(resp.getDomains().contains("mail.uni.ac.uk"));
    }

    @Test
    void testMultipleWebPages() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setName("Web University");
        resp.setWeb_pages(Arrays.asList("https://uni.edu", "https://www.uni.edu", "https://portal.uni.edu"));

        assertEquals(3, resp.getWeb_pages().size());
        assertTrue(resp.getWeb_pages().contains("https://uni.edu"));
        assertTrue(resp.getWeb_pages().contains("https://portal.uni.edu"));
    }

    @Test
    void testNullStateProvince() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setName("Test University");
        resp.setCountry("Australia");
        resp.setState_province(null);

        assertNull(resp.getState_province());
    }

    @Test
    void testDomainListModification() {
        UniversityApiResponse resp = new UniversityApiResponse();
        List<String> domains = new ArrayList<>();
        domains.add("test.edu");
        resp.setDomains(domains);

        assertEquals(1, resp.getDomains().size());

        domains.add("test2.edu");
        assertEquals(2, resp.getDomains().size());
    }

    @Test
    void testWebPageListModification() {
        UniversityApiResponse resp = new UniversityApiResponse();
        List<String> webPages = new ArrayList<>();
        webPages.add("https://test.edu");
        resp.setWeb_pages(webPages);

        assertEquals(1, resp.getWeb_pages().size());

        webPages.add("https://test2.edu");
        assertEquals(2, resp.getWeb_pages().size());
    }

    @Test
    void testAlphaCodeVariations() {
        UniversityApiResponse resp1 = new UniversityApiResponse();
        resp1.setAlpha_two_code("US");

        UniversityApiResponse resp2 = new UniversityApiResponse();
        resp2.setAlpha_two_code("GB");

        UniversityApiResponse resp3 = new UniversityApiResponse();
        resp3.setAlpha_two_code("CA");

        assertEquals("US", resp1.getAlpha_two_code());
        assertEquals("GB", resp2.getAlpha_two_code());
        assertEquals("CA", resp3.getAlpha_two_code());
        assertNotEquals(resp1.getAlpha_two_code(), resp2.getAlpha_two_code());
    }

    @Test
    void testCompleteUniversityMapping() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setName("Complete University");
        resp.setCountry("Netherlands");
        resp.setAlpha_two_code("NL");
        resp.setState_province("North Holland");
        resp.setDomains(Arrays.asList("uni.nl", "uni.edu"));
        resp.setWeb_pages(Arrays.asList("https://uni.nl", "https://www.uni.nl"));

        assertEquals("Complete University", resp.getName());
        assertEquals("Netherlands", resp.getCountry());
        assertEquals("NL", resp.getAlpha_two_code());
        assertEquals("North Holland", resp.getState_province());
        assertEquals(2, resp.getDomains().size());
        assertEquals(2, resp.getWeb_pages().size());
    }

    @Test
    void testUniversityNameVariations() {
        UniversityApiResponse resp1 = new UniversityApiResponse();
        resp1.setName("Oxford University");

        UniversityApiResponse resp2 = new UniversityApiResponse();
        resp2.setName("Cambridge University");

        assertNotEquals(resp1.getName(), resp2.getName());
        assertEquals("Oxford University", resp1.getName());
        assertEquals("Cambridge University", resp2.getName());
    }

    @Test
    void testCountryVariations() {
        String[] countries = {"USA", "UK", "Canada", "Australia", "India", "China"};

        for (int i = 0; i < countries.length; i++) {
            UniversityApiResponse resp = new UniversityApiResponse();
            resp.setCountry(countries[i]);
            assertEquals(countries[i], resp.getCountry());
        }
    }

    @Test
    void testDomainsNotNull() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setDomains(Arrays.asList("test.edu"));
        assertNotNull(resp.getDomains());
    }

    @Test
    void testWebPagesNotNull() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setWeb_pages(Arrays.asList("https://test.edu"));
        assertNotNull(resp.getWeb_pages());
    }

    @Test
    void testSingleDomain() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setDomains(Arrays.asList("single.edu"));

        assertEquals(1, resp.getDomains().size());
        assertEquals("single.edu", resp.getDomains().get(0));
    }

    @Test
    void testSingleWebPage() {
        UniversityApiResponse resp = new UniversityApiResponse();
        resp.setWeb_pages(Arrays.asList("https://single.edu"));

        assertEquals(1, resp.getWeb_pages().size());
        assertEquals("https://single.edu", resp.getWeb_pages().get(0));
    }
}

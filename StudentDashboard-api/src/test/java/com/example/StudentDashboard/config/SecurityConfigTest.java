package com.example.StudentDashboard.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
@WebMvcTest(controllers = {SecurityConfigTest.TestController.class, SecurityConfigTest.H2StubController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test")
        public String get() {
            return "ok";
        }

        @PostMapping("/test")
        public String post() {
            return "ok";
        }
    }

    @RestController
    @RequestMapping("/h2-console")
    static class H2StubController {
        @GetMapping("/dummy")
        public String dummy() {
            return "h2";
        }
    }

  
    

    @Test
    @DisplayName("Frame options headers are disabled")
    void frameOptionsDisabled() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(header().doesNotExist("X-Frame-Options"));
    }
}



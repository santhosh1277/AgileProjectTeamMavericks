import com.example.StudentDashboard.controller.StudentController;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class) // only loads StudentController
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService; // this will be injected into the controller

    @Test
    void testUpdateStudent() throws Exception {
        Student student = new Student("Jane Doe", "2001-02-02", "jane@example.com", "pass123");
        student.setId(1L); // important if your service uses ID

        // Mock the service method
        when(studentService.updateStudentDetails(student)).thenReturn(true);

        mockMvc.perform(put("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().string("Update successful"));
    }
}

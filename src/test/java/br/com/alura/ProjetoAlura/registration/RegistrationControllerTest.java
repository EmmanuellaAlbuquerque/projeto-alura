package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.exceptions.ErrorItemException;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRegistration__should_return_bad_request_when_course_code_is_blank() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("");
        newRegistrationDTO.setStudentEmail("manu@example.com");

        mockMvc.perform(post("/registration/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("courseCode"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createRegistration__should_return_bad_request_when_student_email_is_blank() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("");

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("studentEmail"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createRegistration__should_return_bad_request_when_student_email_is_invalid() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu");

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("studentEmail"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createRegistration__should_return_bad_request_when_student_already_registered() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu@example.com");

        when(registrationService.createRegistration(newRegistrationDTO))
                .thenThrow(new ErrorItemException(
                        new ErrorItemDTO(
                                "studentEmail",
                                "Usuário já está matriculado no curso: " + newRegistrationDTO.getCourseCode()
                        )));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("studentEmail"))
                .andExpect(jsonPath(("$[0].message")).value("Usuário já está matriculado no curso: " + newRegistrationDTO.getCourseCode()));
    }

    @Test
    void createRegistration__should_return_bad_request_when_course_is_inactive() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu@example.com");

        when(registrationService.createRegistration(newRegistrationDTO))
                .thenThrow(new ErrorItemException(
                        new ErrorItemDTO(
                                "courseCode",
                                "Não foi possível se matricular. O Curso está inativo."
                        )));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("courseCode"))
                .andExpect(jsonPath(("$[0].message")).value("Não foi possível se matricular. O Curso está inativo."));
    }

    @Test
    void createRegistration__should_return_created_when_user_request_is_valid() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu@example.com");

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isCreated());
    }
}

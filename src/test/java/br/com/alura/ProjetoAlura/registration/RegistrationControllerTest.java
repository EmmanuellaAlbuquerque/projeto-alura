package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.exceptions.ErrorItemException;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void obtainReport__should_return_all_registrations_as_report() throws Exception {
        List<RegistrationReportItem> reportItemList = new ArrayList<>();

        reportItemList.add(new RegistrationReportItem(
                "Java para Iniciantes",
                "java",
                "Charles",
                "charles@alura.com.br",
                10L
        ));

        reportItemList.add(new RegistrationReportItem(
                "Spring para Iniciantes",
                "spring",
                "Charles",
                "charles@alura.com.br",
                9L
        ));

        reportItemList.add(new RegistrationReportItem(
                "Maven para Avançados",
                "maven",
                "Charles",
                "charles@alura.com.br",
                9L
        ));

        when(registrationService.report()).thenReturn(reportItemList);

        mockMvc.perform(get("/registration/report")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].courseName").value("Java para Iniciantes"))
                .andExpect(jsonPath("$[0].courseCode").value("java"))
                .andExpect(jsonPath("$[0].instructorName").value("Charles"))
                .andExpect(jsonPath("$[0].instructorEmail").value("charles@alura.com.br"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(10L))

                .andExpect(jsonPath("$[1].courseName").value("Spring para Iniciantes"))
                .andExpect(jsonPath("$[1].courseCode").value("spring"))
                .andExpect(jsonPath("$[1].instructorName").value("Charles"))
                .andExpect(jsonPath("$[1].instructorEmail").value("charles@alura.com.br"))
                .andExpect(jsonPath("$[1].totalRegistrations").value(9L))

                .andExpect(jsonPath("$[2].courseName").value("Maven para Avançados"))
                .andExpect(jsonPath("$[2].courseCode").value("maven"))
                .andExpect(jsonPath("$[2].instructorName").value("Charles"))
                .andExpect(jsonPath("$[2].instructorEmail").value("charles@alura.com.br"))
                .andExpect(jsonPath("$[2].totalRegistrations").value(9L));
    }
}

package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.InstructorResponseDTO;
import br.com.alura.ProjetoAlura.util.exceptions.NotFoundException;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCourse__should_return_bad_request_when_name_is_blank() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");

        mockMvc.perform(post("/course/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("name"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_bad_request_when_code_is_blank() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_bad_request_when_code_have_blank_spaces() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java bsc");
        newCourseDTO.setDescription("");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_bad_request_when_code_have_special_caracteres() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java@bsc!");
        newCourseDTO.setDescription("");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_bad_request_when_code_have_numbers() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("javabsc2");
        newCourseDTO.setDescription("");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_bad_request_when_instructor_email_is_blank() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("instructorEmail"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_bad_request_when_instructor_email_is_invalid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("paulo");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("instructorEmail"))
                .andExpect(jsonPath(("$[0].message")).isNotEmpty());
    }

    @Test
    void createCourse__should_return_not_found_when_instructor_does_not_exists() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("test@test.com");

        when(courseService.createCourse(newCourseDTO))
                .thenThrow(new NotFoundException(
                        new ErrorItemDTO(
                                "instructorEmail",
                                "Instrutor não encontrado no sistema")
                        )
                );

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field").value("instructorEmail"))
                .andExpect(jsonPath(("$[0].message")).value("Instrutor não encontrado no sistema"));
    }

    @Test
    void createCourse__should_return_created_when_user_request_is_valid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void findOneCourse__should_return_a_curse_details() throws Exception {
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(
                "Curso Java Básico",
                "java-bsc",
                "Curso introdutório de Java Básico.",
                new InstructorResponseDTO(
                        "Paulo",
                        "paulo.s@test.com"
                ),
                Status.INACTIVE,
                LocalDateTime.now().minusYears(2)
        );

        when(courseService.findOneCourseByCode(courseResponseDTO.code())).thenReturn(courseResponseDTO);

        mockMvc.perform(get("/course?code=" + courseResponseDTO.code()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(courseResponseDTO.name()))
                .andExpect(jsonPath("$.code").value(courseResponseDTO.code()))
                .andExpect(jsonPath("$.description").value(courseResponseDTO.description()))
                .andExpect(jsonPath("$.instructor.name").value(courseResponseDTO.instructor().name()))
                .andExpect(jsonPath("$.instructor.email").value(courseResponseDTO.instructor().email()))
                .andExpect(jsonPath("$.status").value(courseResponseDTO.status().toString()))
                .andExpect(jsonPath("$.inactivationDate").isNotEmpty());
    }

    @Test
    void inactivateCourse__should_return_ok_when_user_request_is_valid() throws Exception {
        String courseCode = "java-c";

        mockMvc.perform(post("/course/" + courseCode + "/inactive"))
                .andExpect(status().isOk());
    }

    @Test
    void inactivateCourse__should_return_not_found_when_code_does_not_exists() throws Exception {
        String courseCode = "java-c";

        when(courseService.inactivateCourse(courseCode))
                .thenThrow(new NotFoundException(
                                new ErrorItemDTO(
                                        "code",
                                        "Código não encontrado no sistema")
                        )
                );

        mockMvc.perform(post("/course/" + courseCode + "/inactive")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath(("$[0].message")).value("Código não encontrado no sistema"));
    }
}

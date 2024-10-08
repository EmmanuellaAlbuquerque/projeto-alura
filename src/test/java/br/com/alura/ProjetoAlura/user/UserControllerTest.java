package br.com.alura.ProjetoAlura.user;

import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import br.com.alura.ProjetoAlura.util.exceptions.ErrorItemException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newStudent__should_return_bad_request_when_password_is_blank() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("test@test.com");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("password"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newStudent__should_return_bad_request_when_email_is_blank() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("email"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newStudent__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("Charles");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("email"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newStudent__should_return_bad_request_when_email_already_exists() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("charles@alura.com.br");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");

        when(userService.createStudent(newStudentUserDTO)).thenThrow(
                new ErrorItemException(
                        new ErrorItemDTO(
                                "email",
                                "Email já cadastrado no sistema"
                        )
                )
        );

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("email"))
                .andExpect(jsonPath("$[0].message").value("Email já cadastrado no sistema"));
    }

    @Test
    void newInstructor__should_return_bad_request_when_email_already_exists() throws Exception {
        NewInstructorUserDTO newInstructorUserDTO = new NewInstructorUserDTO();
        newInstructorUserDTO.setEmail("charles@alura.com.br");
        newInstructorUserDTO.setName("Charles");
        newInstructorUserDTO.setPassword("mudar123");

        when(userService.createInstructor(newInstructorUserDTO)).thenThrow(
                new ErrorItemException(
                        new ErrorItemDTO(
                                "email",
                                "Email já cadastrado no sistema"
                        )
                )
        );

        mockMvc.perform(post("/user/newInstructor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInstructorUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("email"))
                .andExpect(jsonPath("$[0].message").value("Email já cadastrado no sistema"));
    }

    @Test
    void newStudent__should_return_created_when_user_request_is_valid() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("manu@example.com.br");
        newStudentUserDTO.setName("Manu");
        newStudentUserDTO.setPassword("mudar123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void newInstructor__should_return_created_when_user_request_is_valid() throws Exception {
        NewInstructorUserDTO newInstructorUserDTO = new NewInstructorUserDTO();
        newInstructorUserDTO.setEmail("charles@alura.com.br");
        newInstructorUserDTO.setName("Charles");
        newInstructorUserDTO.setPassword("mudar123");

        mockMvc.perform(post("/user/newInstructor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInstructorUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void listAllUsers__should_list_all_users() throws Exception {
        List<UserListItemDTO> usersDTO = new ArrayList<>();
        User user1 = new User("User 1", "user1@test.com", Role.STUDENT,"mudar123");
        User user2 = new User("User 2", "user2@test.com",Role.STUDENT,"mudar123");
        usersDTO.add(new UserListItemDTO(user1));
        usersDTO.add(new UserListItemDTO(user2));

        when(userService.obtainAllUsers()).thenReturn(usersDTO);

        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[0].role").value("STUDENT"))

                .andExpect(jsonPath("$[1].name").value("User 2"))
                .andExpect(jsonPath("$[1].email").value("user2@test.com"))
                .andExpect(jsonPath("$[1].role").value("STUDENT"));
    }

}
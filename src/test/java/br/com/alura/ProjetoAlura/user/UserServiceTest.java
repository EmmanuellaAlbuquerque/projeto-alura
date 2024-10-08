package br.com.alura.ProjetoAlura.user;

import br.com.alura.ProjetoAlura.util.exceptions.ErrorItemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createStudent__should_throws_an_error_when_email_already_exists() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("charles@alura.com.br");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");

        when(userRepository.existsByEmail(newStudentUserDTO.getEmail())).thenReturn(true);

        ErrorItemException exception = assertThrows(
                ErrorItemException.class,
                () -> userService.createStudent(newStudentUserDTO)
        );
        assertEquals("email", exception.getErrorItem().getField());
        assertEquals("Email já cadastrado no sistema", exception.getErrorItem().getMessage());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createStudent__should_create_student_with_success() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("charles@alura.com.br");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");
        ArgumentCaptor<User> studentCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.existsByEmail(newStudentUserDTO.getEmail())).thenReturn(false);
        userService.createStudent(newStudentUserDTO);

        verify(userRepository).save(studentCaptor.capture());
        User capturedStudent = studentCaptor.getValue();
        assertEquals("Charles", capturedStudent.getName());
        assertEquals("charles@alura.com.br", capturedStudent.getEmail());
        assertEquals(Role.STUDENT, capturedStudent.getRole());
        assertNotNull(capturedStudent.getPassword());
        assertNotNull(capturedStudent.getCreatedAt());
    }

    @Test
    void obtainAllUsers__should_return_all_users() {
        User user1 = new User("User 1", "user1@test.com", Role.STUDENT,"mudar123");
        User user2 = new User("User 2", "user2@test.com",Role.STUDENT,"mudar123");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserListItemDTO> users = userService.obtainAllUsers();

        assertEquals(user1.getName(), users.get(0).getName());
        assertEquals(user1.getRole(), users.get(0).getRole());
        assertEquals(user1.getEmail(), users.get(0).getEmail());

        assertEquals(user2.getName(), users.get(1).getName());
        assertEquals(user2.getRole(), users.get(1).getRole());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }
}

package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseService;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.exceptions.ErrorItemException;
import br.com.alura.ProjetoAlura.exceptions.NotFoundException;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserService;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void createRegistration__should_return_not_found_exception_when_student_email_is_not_founded() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu@example.com");

        when(userService.findByEmail(newRegistrationDTO.getStudentEmail()))
                .thenThrow(new NotFoundException(
                        new ErrorItemDTO(
                                "studentEmail",
                                "Usuário não encontrado no sistema"
                        )
                ));

        assertThrows(NotFoundException.class, () -> registrationService.createRegistration(newRegistrationDTO));
        verify(userService).findByEmail(newRegistrationDTO.getStudentEmail());
        verifyNoInteractions(registrationRepository);
    }

    @Test
    void createRegistration__should_return_not_found_exception_when_course_code_is_not_founded() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring");
        newRegistrationDTO.setStudentEmail("manu@example.com");

        when(courseService.findByCode(newRegistrationDTO.getCourseCode()))
                .thenThrow(new NotFoundException(
                        new ErrorItemDTO(
                                "code",
                                "Código não encontrado no sistema"
                        )
                ));

        assertThrows(NotFoundException.class, () -> registrationService.createRegistration(newRegistrationDTO));
        verify(userService).findByEmail(newRegistrationDTO.getStudentEmail());
        verify(courseService).findByCode(newRegistrationDTO.getCourseCode());
        verifyNoInteractions(registrationRepository);
    }

    @Test
    void createRegistration__should_return_error_item_exception_when_student_already_registered() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu@example.com");
        User mockUser = mock(User.class);
        Course mockCourse = mock(Course.class);

        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(mockUser);
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(mockCourse);
        when(registrationRepository.existsByUserAndCourse(mockUser, mockCourse)).thenReturn(true);

        assertThrows(ErrorItemException.class, () -> registrationService.createRegistration(newRegistrationDTO));
        verify(userService).findByEmail(newRegistrationDTO.getStudentEmail());
        verify(courseService).findByCode(newRegistrationDTO.getCourseCode());
        verify(registrationRepository).existsByUserAndCourse(mockUser, mockCourse);
        verifyNoMoreInteractions(registrationRepository);
    }

    @Test
    void createRegistration__should_return_error_item_exception_when_course_is_inactive() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("spring-bsc");
        newRegistrationDTO.setStudentEmail("manu@example.com");
        Course course = new Course();
        course.setStatus(Status.INACTIVE);
        User mockUser = mock(User.class);

        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(mockUser);
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(course);

        assertThrows(ErrorItemException.class, () -> registrationService.createRegistration(newRegistrationDTO));
        verify(userService).findByEmail(newRegistrationDTO.getStudentEmail());
        verify(courseService).findByCode(newRegistrationDTO.getCourseCode());
        verifyNoInteractions(registrationRepository);
    }
}

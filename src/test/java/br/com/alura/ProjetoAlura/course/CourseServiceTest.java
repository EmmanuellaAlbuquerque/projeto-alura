package br.com.alura.ProjetoAlura.course;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse__should_set_status_to_active_when_save_with_success() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");
        User instructor = new User("Paulo", "paulo.s@test.com", Role.INSTRUCTOR, "mudar123");
        when(userService.findInstructorByEmail("paulo.s@test.com")).thenReturn(instructor);

        courseService.createCourse(newCourseDTO);

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseCaptor.capture());
        Course savedCourse = courseCaptor.getValue();
        assertEquals(Status.ACTIVE, savedCourse.getStatus());
    }

    @Test
    void createCourse__should_not_fill_in_inactivation_date_when_save_with_success() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Curso Java Básico");
        newCourseDTO.setCode("java-bsc");
        newCourseDTO.setDescription("Curso introdutório de Java Básico.");
        newCourseDTO.setInstructorEmail("paulo.s@test.com");
        User instructor = new User("Paulo", "paulo.s@test.com", Role.INSTRUCTOR, "mudar123");
        when(userService.findInstructorByEmail("paulo.s@test.com")).thenReturn(instructor);

        courseService.createCourse(newCourseDTO);

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseCaptor.capture());
        Course savedCourse = courseCaptor.getValue();
        assertNull(savedCourse.getInactivationDate());
    }
}
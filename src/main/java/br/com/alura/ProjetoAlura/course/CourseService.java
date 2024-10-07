package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.exceptions.NotFoundException;
import br.com.alura.ProjetoAlura.user.UserService;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CourseService {

    private final UserService userService;
    private final CourseRepository courseRepository;

    public CourseService(UserService userService, CourseRepository courseRepository) {
        this.userService = userService;
        this.courseRepository = courseRepository;
    }

    public Course createCourse(NewCourseDTO newCourse) {

        User instructor = userService.findInstructorByEmail(newCourse.getInstructorEmail());
        Course course = newCourse.toModel(instructor);

        return courseRepository.save(course);
    }

    public Course inactivateCourse(String courseCode) {

        Course course = this.findByCode(courseCode);

        course.setStatus(Status.INACTIVE);
        course.setInactivationDate(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public Course findByCode(String courseCode) {
        return courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorItemDTO(
                                "code",
                                "Código não encontrado no sistema"
                        )
                ));
    }
}

package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.UserService;
import br.com.alura.ProjetoAlura.user.User;
import org.springframework.stereotype.Service;

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
}

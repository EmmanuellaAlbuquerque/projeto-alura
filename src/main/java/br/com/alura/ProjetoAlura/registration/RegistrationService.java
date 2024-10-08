package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseService;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.util.exceptions.ErrorItemException;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserService;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseService courseService;
    private final UserService userService;

    public RegistrationService(RegistrationRepository registrationRepository,
                               CourseService courseService,
                               UserService userService) {
        this.registrationRepository = registrationRepository;
        this.courseService = courseService;
        this.userService = userService;
    }

    public Registration createRegistration(NewRegistrationDTO newRegistration) {

        User student = this.userService.findByEmail(newRegistration.getStudentEmail());
        Course course = this.courseService.findByCode(newRegistration.getCourseCode());

        if (course.getStatus() == Status.INACTIVE) {
            throw new ErrorItemException(
                    new ErrorItemDTO(
                            "courseCode",
                            "Não foi possível se matricular. O Curso está inativo."
                    )
            );
        }

        if (this.registrationRepository.existsByStudentAndCourse(student, course)) {
            throw new ErrorItemException(
                    new ErrorItemDTO(
                            "studentEmail",
                            "Usuário já está matriculado no curso: " + course.getCode()
                    )
            );
        }

        Registration registration = newRegistration.toModel(student, course);
        return registrationRepository.save(registration);
    }

    public List<RegistrationReportItem> report() {

        List<RegistrationReportProjection> reportProjections = this.registrationRepository.findAllGroupByCourseAndOrderByStudentsCount();

        return reportProjections.stream()
                .map(reportProjection -> new RegistrationReportItem(
                        reportProjection.getCourseName(),
                        reportProjection.getCourseCode(),
                        reportProjection.getInstructorName(),
                        reportProjection.getInstructorEmail(),
                        reportProjection.getTotalRegistrations()
                )).toList();

    }
}

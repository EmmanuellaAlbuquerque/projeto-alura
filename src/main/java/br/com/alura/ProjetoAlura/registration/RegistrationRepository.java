package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByStudentAndCourse(User student, Course course);

    @Query(value =
            """
                SELECT COUNT(student_id) AS totalRegistrations, course.name AS courseName, course.code AS courseCode, user.name AS instructorName, user.email AS instructorEmail FROM registration
                INNER JOIN course ON registration.course_id=course.id
                INNER JOIN user ON course.instructor_id=user.id
                GROUP BY course_id ORDER BY totalRegistrations DESC;
            """, nativeQuery = true)
    List<RegistrationReportProjection> findAllGroupByCourseAndOrderByStudentsCount();
}

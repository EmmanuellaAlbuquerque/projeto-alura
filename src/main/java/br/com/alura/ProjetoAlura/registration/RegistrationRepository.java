package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndCourse(User user, Course course);

    @Query(value =
            """
                SELECT COUNT(user_id) as totalRegistrations, course.name as courseName, course.code as courseCode, user.name as instructorName, user.email as instructorEmail FROM registration
                INNER JOIN course ON registration.course_id=course.id
                INNER JOIN user ON course.instructor_id=user.id
                GROUP BY course_id ORDER BY totalRegistrations DESC;
            """, nativeQuery = true)
    List<RegistrationReportProjection> findAllGroupByCourseAndOrderByStudentsCount();
}

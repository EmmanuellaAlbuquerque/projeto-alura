package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class NewRegistrationDTO {

    @NotBlank
    @NotNull
    private String courseCode;

    @NotBlank
    @NotNull
    @Email
    private String studentEmail;

    public NewRegistrationDTO() {}

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Registration toModel(User user, Course course) {
        return new Registration(
                user,
                course
        );
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof NewRegistrationDTO that)) return false;
        return Objects.equals(getCourseCode(), that.getCourseCode()) && Objects.equals(getStudentEmail(), that.getStudentEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseCode(), getStudentEmail());
    }
}

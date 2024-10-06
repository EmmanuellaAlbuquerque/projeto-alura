package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class NewCourseDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Length(min = 4, max = 10)
    @Pattern(regexp = "^[a-z]+(-[a-z]+)*$")
    private String code;

    private String description;

    @NotNull
    @NotBlank
    @Email
    private String instructorEmail;

    public NewCourseDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof NewCourseDTO that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getCode(), that.getCode()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getInstructorEmail(), that.getInstructorEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCode(), getDescription(), getInstructorEmail());
    }

    public Course toModel(User instructor) {
        return new Course(name, code, instructor, description, Status.ACTIVE);
    }
}

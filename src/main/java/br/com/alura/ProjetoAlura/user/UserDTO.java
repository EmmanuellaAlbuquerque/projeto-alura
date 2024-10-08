package br.com.alura.ProjetoAlura.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public abstract class UserDTO {

    @NotNull
    @Length(min = 3, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Length(min = 8, max = 16)
    private String password;

    public UserDTO() {}

    abstract User toModel();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserDTO userDTO)) return false;
        return Objects.equals(getName(), userDTO.getName()) && Objects.equals(getEmail(), userDTO.getEmail()) && Objects.equals(getPassword(), userDTO.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPassword());
    }
}

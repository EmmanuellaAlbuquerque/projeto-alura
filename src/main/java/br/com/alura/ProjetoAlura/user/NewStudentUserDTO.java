package br.com.alura.ProjetoAlura.user;

import static br.com.alura.ProjetoAlura.user.Role.STUDENT;

public class NewStudentUserDTO extends UserDTO {

    @Override
    public User toModel() {
        return new User(super.getName(), super.getEmail(), STUDENT, super.getPassword());
    }
}

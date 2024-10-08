package br.com.alura.ProjetoAlura.user;

import static br.com.alura.ProjetoAlura.user.Role.INSTRUCTOR;

public class NewInstructorUserDTO extends UserDTO {

    @Override
    User toModel() {
        return new User(super.getName(), super.getEmail(), INSTRUCTOR, super.getPassword());
    }
}

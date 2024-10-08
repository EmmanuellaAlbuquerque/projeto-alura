package br.com.alura.ProjetoAlura.user;

import br.com.alura.ProjetoAlura.util.exceptions.ErrorItemException;
import br.com.alura.ProjetoAlura.util.exceptions.NotFoundException;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findInstructorByEmail(String email) {

       return userRepository.findByEmailAndRole(email, Role.INSTRUCTOR)
               .orElseThrow(() -> new NotFoundException(
                       new ErrorItemDTO("instructorEmail", "Instrutor não encontrado no sistema")
               ));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        new ErrorItemDTO("studentEmail", "Usuário não encontrado no sistema")
                ));
    }

    private void verifyIfEmailAlreadyInUse(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new ErrorItemException(
                    new ErrorItemDTO(
                            "email",
                            "Email já cadastrado no sistema")
            );
        }
    }

    public User createStudent(NewStudentUserDTO newStudent) {

        verifyIfEmailAlreadyInUse(newStudent.getEmail());
        User user = newStudent.toModel();
        return userRepository.save(user);
    }

    public User createInstructor(NewInstructorUserDTO newInstructor) {

        verifyIfEmailAlreadyInUse(newInstructor.getEmail());
        User user = newInstructor.toModel();
        return userRepository.save(user);
    }

    public List<UserListItemDTO> obtainAllUsers() {
        return userRepository.findAll().stream().map(UserListItemDTO::new).toList();
    }
}

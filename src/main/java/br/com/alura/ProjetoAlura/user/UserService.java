package br.com.alura.ProjetoAlura.user;

import br.com.alura.ProjetoAlura.exceptions.NotFoundException;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import org.springframework.stereotype.Service;

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
}

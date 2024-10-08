package br.com.alura.ProjetoAlura.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/user/newStudent")
    public ResponseEntity<Void> newStudent(@RequestBody @Valid NewStudentUserDTO newStudent) {

        this.userService.createStudent(newStudent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/user/newInstructor")
    public ResponseEntity<Void> newInstructor(@RequestBody @Valid NewInstructorUserDTO newInstructor) {
        this.userService.createInstructor(newInstructor);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/all")
    public List<UserListItemDTO> listAllUsers() {
        return this.userService.obtainAllUsers();
    }
}

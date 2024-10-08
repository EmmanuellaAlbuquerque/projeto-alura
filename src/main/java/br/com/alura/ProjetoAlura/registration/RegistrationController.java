package br.com.alura.ProjetoAlura.registration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Transactional
    @PostMapping("/registration/new")
    public ResponseEntity<Void> createRegistration(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        this.registrationService.createRegistration(newRegistration);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> obtainReport() {

        List<RegistrationReportItem> items = this.registrationService.report();
        return ResponseEntity.ok(items);
    }

}

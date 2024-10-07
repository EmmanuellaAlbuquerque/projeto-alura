package br.com.alura.ProjetoAlura.util;

import br.com.alura.ProjetoAlura.exceptions.ErrorItemException;
import br.com.alura.ProjetoAlura.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorItemDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorItemDTO> errors = ex.getBindingResult().getFieldErrors().stream().map(ErrorItemDTO::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<List<ErrorItemDTO>> handleNotFoundException(NotFoundException ex) {
        ErrorItemDTO error = ex.getErrorItem();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of(error));
    }

    @ExceptionHandler(ErrorItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorItemDTO>> handleErrorItemException(ErrorItemException ex) {
        ErrorItemDTO error = ex.getErrorItem();
        return ResponseEntity.badRequest().body(List.of(error));
    }
}

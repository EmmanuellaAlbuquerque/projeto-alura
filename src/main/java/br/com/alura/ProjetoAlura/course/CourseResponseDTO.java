package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.InstructorResponseDTO;

import java.time.LocalDateTime;

public record CourseResponseDTO(
        String name,
        String code,
        String description,
        InstructorResponseDTO instructor,
        Status status,
        LocalDateTime inactivationDate
) {}

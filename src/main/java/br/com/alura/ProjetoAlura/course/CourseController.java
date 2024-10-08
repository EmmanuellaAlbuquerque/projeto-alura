package br.com.alura.ProjetoAlura.course;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/course")
@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Transactional
    @PostMapping("/new")
    public ResponseEntity<Void> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        this.courseService.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/{code}/inactive")
    public ResponseEntity<Void> inactivateCourse(@PathVariable("code") String courseCode) {
        this.courseService.inactivateCourse(courseCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CourseResponseDTO> findOneCourse(@RequestParam("code") String courseCode) {
        return ResponseEntity.ok().body(this.courseService.findOneCourseByCode(courseCode));
    }
}

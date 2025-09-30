package com.example.sms.controller;

import com.example.sms.dto.CourseCreateDto;
import com.example.sms.dto.CourseDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public CourseDto create(@Valid @RequestBody CourseCreateDto dto) {
        return courseService.create(dto);
    }

    @GetMapping("/{id}")
    public CourseDto getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @GetMapping
    public List<CourseDto> getAll() {
        return courseService.getAll();
    }

    @PutMapping("/{id}")
    public CourseDto update(@PathVariable Long id, @Valid @RequestBody CourseCreateDto dto) {
        return courseService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public void enroll(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.enrollStudent(courseId, studentId);
    }

    @PostMapping("/{courseId}/unenroll/{studentId}")
    public void unenroll(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.unenrollStudent(courseId, studentId);
    }

    @GetMapping("/{courseId}/students")
    public List<StudentDto> getStudentsFiltered(@PathVariable Long courseId, @RequestParam(required = false) Double minGpa) {
        return courseService.getStudentsFiltered(courseId, minGpa);
    }

    @PostMapping("/{courseId}/assign-instructor/{instructorId}")
    public void assignInstructor(@PathVariable Long courseId, @PathVariable Long instructorId) {
        courseService.assignInstructor(courseId, instructorId);
    }

    @PostMapping("/{courseId}/unassign-instructor")
    public void unassignInstructor(@PathVariable Long courseId) {
        courseService.unassignInstructor(courseId);
    }
}

package com.example.sms.service;

import com.example.sms.dto.CourseCreateDto;
import com.example.sms.dto.CourseDto;
import com.example.sms.dto.StudentDto;
import java.util.List;

public interface CourseService {
    CourseDto create(CourseCreateDto dto);
    CourseDto getById(Long id);
    List<CourseDto> getAll();
    CourseDto update(Long id, CourseCreateDto dto);
    void delete(Long id);

    void enrollStudent(Long courseId, Long studentId);
    void unenrollStudent(Long courseId, Long studentId);
    List<StudentDto> getStudentsFiltered(Long courseId, Double minGpa);

    void assignInstructor(Long courseId, Long instructorId);
    void unassignInstructor(Long courseId);
}

package com.example.sms.service.impl;

import com.example.sms.dto.CourseCreateDto;
import com.example.sms.dto.CourseDto;
import com.example.sms.entity.Course;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDto create(CourseCreateDto dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return map(courseRepository.save(course));
    }

    @Override
    public CourseDto getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return map(course);
    }

    @Override
    public List<CourseDto> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto update(Long id, CourseCreateDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return map(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    private CourseDto map(Course course) {
        return new CourseDto(course.getId(), course.getName(), course.getDescription());
    }
}

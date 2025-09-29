package com.example.sms.service;

import com.example.sms.dto.CourseCreateDto;
import com.example.sms.dto.CourseDto;
import java.util.List;

public interface CourseService {
    CourseDto create(CourseCreateDto dto);
    CourseDto getById(Long id);
    List<CourseDto> getAll();
    CourseDto update(Long id, CourseCreateDto dto);
    void delete(Long id);
}

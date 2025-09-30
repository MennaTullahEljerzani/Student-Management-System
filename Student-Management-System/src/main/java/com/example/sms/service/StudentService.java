package com.example.sms.service;

import com.example.sms.dto.StudentCreateDto;
import com.example.sms.dto.StudentDto;

import java.util.List;

public interface StudentService {
    StudentDto create(StudentCreateDto dto);
    StudentDto getById(Long id);
    List<StudentDto> getAll();
    List<StudentDto> getAllSortedByGpaDesc();
    StudentDto update(Long id, StudentCreateDto dto);
    void delete(Long id);
}

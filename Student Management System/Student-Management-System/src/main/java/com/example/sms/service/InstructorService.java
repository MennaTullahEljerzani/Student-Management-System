package com.example.sms.service;

import com.example.sms.dto.InstructorDto;
import com.example.sms.dto.InstructorCreateDto;

import java.util.List;

public interface InstructorService {
    InstructorDto create(InstructorCreateDto dto);
    InstructorDto getById(Long id);
    List<InstructorDto> getAll();
    InstructorDto update(Long id, InstructorCreateDto dto);
    void delete(Long id);
}

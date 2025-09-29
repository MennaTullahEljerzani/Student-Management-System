package com.example.sms.service.impl;

import com.example.sms.dto.InstructorCreateDto;
import com.example.sms.dto.InstructorDto;
import com.example.sms.entity.Instructor;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.InstructorRepository;
import com.example.sms.service.InstructorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository repo;

    public InstructorServiceImpl(InstructorRepository repo) {
        this.repo = repo;
    }

    @Override
    public InstructorDto create(InstructorCreateDto dto) {
        Instructor instructor = new Instructor(dto.getFirstName(), dto.getLastName(), dto.getEmail());
        return map(repo.save(instructor));
    }

    @Override
    public InstructorDto getById(Long id) {
        Instructor instructor = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id " + id));
        return map(instructor);
    }

    @Override
    public List<InstructorDto> getAll() {
        return repo.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public InstructorDto update(Long id, InstructorCreateDto dto) {
        Instructor instructor = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        instructor.setFirstName(dto.getFirstName());
        instructor.setLastName(dto.getLastName());
        instructor.setEmail(dto.getEmail());
        return map(repo.save(instructor));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Instructor not found");
        }
        repo.deleteById(id);
    }

    private InstructorDto map(Instructor instructor) {
        InstructorDto dto = new InstructorDto();
        dto.setId(instructor.getId());
        dto.setFirstName(instructor.getFirstName());
        dto.setLastName(instructor.getLastName());
        dto.setEmail(instructor.getEmail());
        return dto;
    }
}

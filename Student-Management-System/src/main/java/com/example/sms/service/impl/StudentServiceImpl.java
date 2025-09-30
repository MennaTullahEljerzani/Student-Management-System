package com.example.sms.service.impl;

import com.example.sms.dto.StudentCreateDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Student;
import com.example.sms.exception.BadRequestException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.StudentService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StudentServiceImpl(StudentRepository repo) { this.repo = repo; }

    @Override
    public StudentDto create(StudentCreateDto dto) {
        if (repo.existsByEmail(dto.getEmail())) throw new BadRequestException("Email already used");
        String hashed = passwordEncoder.encode(dto.getPassword());
        Student s = new Student(dto.getFirstName(), dto.getLastName(), dto.getEmail(), hashed);
        Student saved = repo.save(s);
        return map(saved);
    }

    @Override
    public StudentDto getById(Long id) {
        Student s = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return map(s);
    }

    @Override
    public List<StudentDto> getAll() {
        return repo.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getAllSortedByGpaDesc() {
        return repo.findAll().stream()
                .sorted(Comparator.comparing((Student st) -> st.getGpa() == null ? 0.0 : st.getGpa()).reversed())
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto update(Long id, StudentCreateDto dto) {
        Student s = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (!s.getEmail().equals(dto.getEmail()) && repo.existsByEmail(dto.getEmail()))
            throw new BadRequestException("Email already in use");
        s.setFirstName(dto.getFirstName());
        s.setLastName(dto.getLastName());
        s.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            s.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        return map(repo.save(s));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Student not found");
        repo.deleteById(id);
    }

    private StudentDto map(Student s) {
        StudentDto dto = new StudentDto();
        dto.setId(s.getId());
        dto.setFirstName(s.getFirstName());
        dto.setLastName(s.getLastName());
        dto.setEmail(s.getEmail());
        dto.setGpa(s.getGpa());
        return dto;
    }
}

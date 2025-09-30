package com.example.sms.controller;

import com.example.sms.dto.InstructorCreateDto;
import com.example.sms.dto.InstructorDto;
import com.example.sms.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public InstructorDto create(@Valid @RequestBody InstructorCreateDto dto) {
        return instructorService.create(dto);
    }

    @GetMapping("/{id}")
    public InstructorDto getById(@PathVariable Long id) {
        return instructorService.getById(id);
    }

    @GetMapping
    public List<InstructorDto> getAll() {
        return instructorService.getAll();
    }

    @PutMapping("/{id}")
    public InstructorDto update(@PathVariable Long id, @Valid @RequestBody InstructorCreateDto dto) {
        return instructorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        instructorService.delete(id);
    }
}

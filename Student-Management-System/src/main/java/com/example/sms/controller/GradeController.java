package com.example.sms.controller;

import com.example.sms.entity.Grade;
import com.example.sms.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/assign")
    public ResponseEntity<Grade> assign(@RequestParam Long studentId,
                                        @RequestParam Long courseId,
                                        @RequestParam(name = "score") Double score,
                                        @RequestParam(required = false) String term) {
        return ResponseEntity.ok(gradeService.assignGrade(studentId, courseId, score, term));
    }

    @GetMapping("/report/{studentId}")
    public ResponseEntity<List<Grade>> report(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getReportCard(studentId));
    }

    @GetMapping("/gpa/{studentId}")
    public ResponseEntity<Double> gpa(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.computeGpaForStudent(studentId));
    }
} 
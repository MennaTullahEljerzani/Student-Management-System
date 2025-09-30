package com.example.sms.service;

import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Grade;

import java.util.List;

public interface GradeService {
    Grade assignGrade(Long studentId, Long courseId, Double value, String term);
    Double computeGpaForStudent(Long studentId);
    List<Grade> getReportCard(Long studentId);
} 
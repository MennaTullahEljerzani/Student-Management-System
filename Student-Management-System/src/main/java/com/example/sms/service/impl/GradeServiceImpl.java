package com.example.sms.service.impl;

import com.example.sms.entity.Course;
import com.example.sms.entity.Grade;
import com.example.sms.entity.Student;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.GradeRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeServiceImpl(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public Grade assignGrade(Long studentId, Long courseId, Double value, String term) {
        if (value == null || value < 0 || value > 100.0) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        Grade grade = gradeRepository.findByStudentAndCourse(student, course).orElse(new Grade());
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(value);
        grade.setTerm(term);
        Grade saved = gradeRepository.save(grade);

        // recompute GPA and persist on student
        Double gpa = computeGpaForStudent(studentId);
        student.setGpa(gpa);
        studentRepository.save(student);

        return saved;
    }

    @Override
    public Double computeGpaForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        List<Grade> grades = gradeRepository.findByStudent(student);
        if (grades.isEmpty()) return 0.0;
        double avgGpa = grades.stream()
                .mapToDouble(g -> mapScoreToGpa(g.getScore()))
                .average().orElse(0.0);
        return Math.round(avgGpa * 100.0) / 100.0; // round to 2 decimals
    }

    @Override
    public List<Grade> getReportCard(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return gradeRepository.findByStudent(student);
    }

    private double mapScoreToGpa(Double score) {
        double s = score == null ? 0.0 : score;
        if (s >= 93) return 4.0;
        if (s >= 90) return 3.7;
        if (s >= 87) return 3.3;
        if (s >= 83) return 3.0;
        if (s >= 80) return 2.7;
        if (s >= 77) return 2.3;
        if (s >= 73) return 2.0;
        if (s >= 70) return 1.7;
        if (s >= 67) return 1.3;
        if (s >= 65) return 1.0;
        return 0.0;
    }
} 
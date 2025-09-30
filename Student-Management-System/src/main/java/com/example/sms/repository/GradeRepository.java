package com.example.sms.repository;

import com.example.sms.entity.Grade;
import com.example.sms.entity.Student;
import com.example.sms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByStudentAndCourse(Student student, Course course);
    List<Grade> findByStudent(Student student);
} 
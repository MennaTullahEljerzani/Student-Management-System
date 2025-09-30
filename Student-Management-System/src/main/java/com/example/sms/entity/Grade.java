package com.example.sms.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "grades", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@JsonIgnoreProperties({"courses", "passwordHash"})
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    // numeric score 0 - 100
    @Column(name = "score", nullable = false)
    private Double score;

    @Column
    private String term; // e.g., "2025-Spring"

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
} 
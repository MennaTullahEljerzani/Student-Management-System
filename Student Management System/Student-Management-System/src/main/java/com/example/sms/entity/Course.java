package com.example.sms.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.example.sms.entity.Student;
import com.example.sms.entity.Instructor;


@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // Many-to-Many with Students
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    // Many-to-One with Instructor
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // --- Constructors ---
    public Course() {}

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}

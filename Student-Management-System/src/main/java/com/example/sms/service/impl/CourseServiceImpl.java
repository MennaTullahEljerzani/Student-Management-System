package com.example.sms.service.impl;

import com.example.sms.dto.CourseCreateDto;
import com.example.sms.dto.CourseDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Course;
import com.example.sms.entity.Student;
import com.example.sms.entity.Instructor;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.InstructorRepository;
import com.example.sms.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    public CourseServiceImpl(CourseRepository courseRepository, StudentRepository studentRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public CourseDto create(CourseCreateDto dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return map(courseRepository.save(course));
    }

    @Override
    public CourseDto getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return map(course);
    }

    @Override
    public List<CourseDto> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto update(Long id, CourseCreateDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return map(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public void enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        course.getStudents().add(student);
        student.getCourses().add(course);
        courseRepository.save(course);
    }

    @Override
    public void unenrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        course.getStudents().remove(student);
        student.getCourses().remove(course);
        courseRepository.save(course);
    }

    @Override
    public List<StudentDto> getStudentsFiltered(Long courseId, Double minGpa) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        return course.getStudents().stream()
                .filter(s -> minGpa == null || (s.getGpa() != null && s.getGpa() >= minGpa))
                .sorted((a, b) -> Double.compare(b.getGpa() == null ? 0.0 : b.getGpa(), a.getGpa() == null ? 0.0 : a.getGpa()))
                .map(this::mapStudent)
                .collect(Collectors.toList());
    }

    @Override
    public void assignInstructor(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + instructorId));
        course.setInstructor(instructor);
        courseRepository.save(course);
    }

    @Override
    public void unassignInstructor(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        course.setInstructor(null);
        courseRepository.save(course);
    }

    private CourseDto map(Course course) {
        return new CourseDto(course.getId(), course.getName(), course.getDescription());
    }

    private StudentDto mapStudent(Student s) {
        StudentDto dto = new StudentDto();
        dto.setId(s.getId());
        dto.setFirstName(s.getFirstName());
        dto.setLastName(s.getLastName());
        dto.setEmail(s.getEmail());
        dto.setGpa(s.getGpa());
        return dto;
    }
}

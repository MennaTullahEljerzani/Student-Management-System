package com.example.sms.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlowIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void endToEnd_register_enroll_assignGrade_viewGpaAndReport() {
        // 1) Register student
        Map<String, Object> studentReq = new HashMap<>();
        studentReq.put("firstName", "John");
        studentReq.put("lastName", "Doe");
        studentReq.put("email", "john.doe@example.com");
        studentReq.put("password", "secret123");

        ResponseEntity<Map<String, Object>> studentResp = rest.postForEntity(url("/api/students"), studentReq, (Class<Map<String, Object>>)(Class<?>)Map.class);
        assertThat(studentResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(studentResp.getBody()).isNotNull();
        Number studentId = (Number) studentResp.getBody().get("id");
        assertThat(studentId).isNotNull();

        // 2) Create instructor
        Map<String, Object> instructorReq = new HashMap<>();
        instructorReq.put("firstName", "Alice");
        instructorReq.put("lastName", "Smith");
        instructorReq.put("email", "alice.smith@example.com");

        ResponseEntity<Map<String, Object>> instructorResp = rest.postForEntity(url("/api/instructors"), instructorReq, (Class<Map<String, Object>>)(Class<?>)Map.class);
        assertThat(instructorResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(instructorResp.getBody()).isNotNull();
        Number instructorId = (Number) instructorResp.getBody().get("id");
        assertThat(instructorId).isNotNull();

        // 3) Create course
        Map<String, Object> courseReq = new HashMap<>();
        courseReq.put("name", "Math 101");
        courseReq.put("description", "Basic Math");

        ResponseEntity<Map<String, Object>> courseResp = rest.postForEntity(url("/api/courses"), courseReq, (Class<Map<String, Object>>)(Class<?>)Map.class);
        assertThat(courseResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(courseResp.getBody()).isNotNull();
        Number courseId = (Number) courseResp.getBody().get("id");
        assertThat(courseId).isNotNull();

        // 4) Assign instructor to course
        rest.postForEntity(url("/api/courses/" + courseId + "/assign-instructor/" + instructorId), null, Void.class);

        // 5) Enroll student in course
        rest.postForEntity(url("/api/courses/" + courseId + "/enroll/" + studentId), null, Void.class);

        // 6) Assign grade
        String assignUrl = url("/api/grades/assign?studentId=" + studentId + "&courseId=" + courseId + "&score=95&term=2025-Spring");
        ResponseEntity<Map<String, Object>> assignResp = rest.postForEntity(assignUrl, null, (Class<Map<String, Object>>)(Class<?>)Map.class);
        assertThat(assignResp.getStatusCode().is2xxSuccessful()).isTrue();

        // 7) Verify GPA
        ResponseEntity<Double> gpaResp = rest.getForEntity(url("/api/grades/gpa/" + studentId), Double.class);
        assertThat(gpaResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(gpaResp.getBody()).isNotNull();
        assertThat(gpaResp.getBody()).isGreaterThan(0.0);

        // 8) Verify report card has one entry
        ResponseEntity<List> reportResp = rest.getForEntity(url("/api/grades/report/" + studentId), List.class);
        assertThat(reportResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(reportResp.getBody()).isNotNull();
        assertThat(reportResp.getBody().size()).isEqualTo(1);
        @SuppressWarnings("unchecked") Map<String, Object> firstGrade = (Map<String, Object>) reportResp.getBody().get(0);
        assertThat(((Number) firstGrade.get("score")).doubleValue()).isEqualTo(95.0);
    }
}



package com.example.sms.dto;

public class CourseCreateDto {
    private String name;
    private String description;

    public CourseCreateDto() {}

    public CourseCreateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

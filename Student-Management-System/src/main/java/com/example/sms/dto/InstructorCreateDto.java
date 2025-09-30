package com.example.sms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InstructorCreateDto {
    @NotBlank @Size(max = 60)
    private String firstName;
    @NotBlank @Size(max = 60)
    private String lastName;
    @Email @NotBlank @Size(max = 120)
    private String email;

    // --- Getters & Setters ---
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

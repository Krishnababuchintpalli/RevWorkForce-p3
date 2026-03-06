package com.revworkforce.performance.dto;

public class EmployeeDto {

    private Long id;
    private String fullName;
    private Long managerId;

    public EmployeeDto() {
    }

    public EmployeeDto(Long id, String fullName, Long managerId) {
        this.id = id;
        this.fullName = fullName;
        this.managerId = managerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}

package com.revworkforce.admin.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    private String name;

    public DepartmentRequest() {
    }

    public DepartmentRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentRequest build() {
            return new DepartmentRequest(name);
        }
    }
}

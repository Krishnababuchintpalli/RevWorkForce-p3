package com.revworkforce.admin.dto;

public class DepartmentResponse {

    private Long id;
    private String name;

    public DepartmentResponse() {
    }

    public DepartmentResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentResponse build() {
            return new DepartmentResponse(id, name);
        }
    }
}

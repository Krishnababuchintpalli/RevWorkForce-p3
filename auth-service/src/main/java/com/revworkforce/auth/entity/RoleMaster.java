package com.revworkforce.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_master")
public class RoleMaster {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    public RoleMaster() {
    }

    public RoleMaster(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public RoleMaster build() {
            return new RoleMaster(id, name);
        }
    }
}

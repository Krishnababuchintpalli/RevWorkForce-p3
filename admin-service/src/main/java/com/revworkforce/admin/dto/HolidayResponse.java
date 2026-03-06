package com.revworkforce.admin.dto;

import java.time.LocalDate;

public class HolidayResponse {

    private Long id;
    private String name;
    private LocalDate date;

    public HolidayResponse() {
    }

    public HolidayResponse(Long id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private LocalDate date;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public HolidayResponse build() {
            return new HolidayResponse(id, name, date);
        }
    }
}

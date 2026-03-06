package com.revworkforce.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnnouncementRequest {

    @NotBlank(message = "Announcement title is required")
    private String title;

    @NotBlank(message = "Announcement content is required")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    private String content;

    public AnnouncementRequest() {
    }

    public AnnouncementRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String content;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public AnnouncementRequest build() {
            return new AnnouncementRequest(title, content);
        }
    }
}

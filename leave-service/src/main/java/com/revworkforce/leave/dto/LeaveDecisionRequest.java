package com.revworkforce.leave.dto;

import jakarta.validation.constraints.Size;

public class LeaveDecisionRequest {

    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;

    // Constructors
    public LeaveDecisionRequest() {
    }

    public LeaveDecisionRequest(String comment) {
        this.comment = comment;
    }

    // Getters and Setters
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

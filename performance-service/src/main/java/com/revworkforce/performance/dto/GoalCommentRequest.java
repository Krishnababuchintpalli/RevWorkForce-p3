package com.revworkforce.performance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GoalCommentRequest {

    @NotBlank(message = "Manager comment is required")
    @Size(max = 2000, message = "Manager comment must not exceed 2000 characters")
    private String managerComment;

    public GoalCommentRequest() {
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }
}

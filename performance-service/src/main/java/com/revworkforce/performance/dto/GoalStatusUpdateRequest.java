package com.revworkforce.performance.dto;

import com.revworkforce.performance.enums.GoalStatus;
import jakarta.validation.constraints.NotNull;

public class GoalStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private GoalStatus status;

    public GoalStatusUpdateRequest() {
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }
}

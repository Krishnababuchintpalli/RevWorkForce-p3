package com.revworkforce.leave.dto;

import java.util.List;

public class TeamLeaveResponse {

    private List<LeaveResponse> leaves;

    // Constructors
    public TeamLeaveResponse() {
    }

    public TeamLeaveResponse(List<LeaveResponse> leaves) {
        this.leaves = leaves;
    }

    // Getters and Setters
    public List<LeaveResponse> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<LeaveResponse> leaves) {
        this.leaves = leaves;
    }
}

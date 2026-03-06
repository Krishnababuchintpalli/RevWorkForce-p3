package com.revworkforce.performance.dto;

import java.util.List;

public class TeamGoalResponse {

    private List<GoalResponse> goals;

    public TeamGoalResponse() {
    }

    public TeamGoalResponse(List<GoalResponse> goals) {
        this.goals = goals;
    }

    public List<GoalResponse> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalResponse> goals) {
        this.goals = goals;
    }
}

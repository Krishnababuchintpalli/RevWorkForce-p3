package com.revworkforce.performance.controller;

import com.revworkforce.performance.dto.GoalCommentRequest;
import com.revworkforce.performance.dto.GoalRequest;
import com.revworkforce.performance.dto.GoalResponse;
import com.revworkforce.performance.dto.GoalStatusUpdateRequest;
import com.revworkforce.performance.dto.TeamGoalResponse;
import com.revworkforce.performance.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody GoalRequest request) {
        GoalResponse response = goalService.createGoal(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<GoalResponse>> getMyGoals(
            @RequestHeader("X-User-Id") Long userId) {
        List<GoalResponse> goals = goalService.getMyGoals(userId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/team")
    public ResponseEntity<TeamGoalResponse> getTeamGoals(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {
        TeamGoalResponse response = goalService.getTeamGoals(userId, role);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GoalResponse> updateGoalStatus(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody GoalStatusUpdateRequest request) {
        GoalResponse response = goalService.updateGoalStatus(userId, id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/comment")
    public ResponseEntity<GoalResponse> addManagerComment(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @Valid @RequestBody GoalCommentRequest request) {
        GoalResponse response = goalService.addManagerComment(userId, id, request, role);
        return ResponseEntity.ok(response);
    }
}

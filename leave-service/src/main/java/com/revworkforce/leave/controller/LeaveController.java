package com.revworkforce.leave.controller;

import com.revworkforce.leave.dto.*;
import com.revworkforce.leave.service.LeaveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public ResponseEntity<LeaveResponse> applyLeave(
            @Valid @RequestBody LeaveApplyRequest request,
            HttpServletRequest httpRequest) {

        Long userId = getUserIdFromRequest(httpRequest);
        LeaveResponse response = leaveService.applyLeave(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        List<LeaveResponse> leaves = leaveService.getMyLeaves(userId);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/summary")
    public ResponseEntity<LeaveSummaryResponse> getLeaveSummary(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        LeaveSummaryResponse summary = leaveService.getLeaveSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/team")
    public ResponseEntity<TeamLeaveResponse> getTeamLeaves(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        String role = getRoleFromRequest(httpRequest);
        TeamLeaveResponse teamLeaves = leaveService.getTeamLeaves(userId, role);
        return ResponseEntity.ok(teamLeaves);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LeaveResponse> cancelLeave(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long userId = getUserIdFromRequest(httpRequest);
        LeaveResponse response = leaveService.cancelLeave(userId, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(
            @PathVariable Long id,
            @Valid @RequestBody LeaveDecisionRequest request,
            HttpServletRequest httpRequest) {

        Long userId = getUserIdFromRequest(httpRequest);
        String role = getRoleFromRequest(httpRequest);
        LeaveResponse response = leaveService.approveLeave(userId, id, request, role);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(
            @PathVariable Long id,
            @Valid @RequestBody LeaveDecisionRequest request,
            HttpServletRequest httpRequest) {

        Long userId = getUserIdFromRequest(httpRequest);
        String role = getRoleFromRequest(httpRequest);
        LeaveResponse response = leaveService.rejectLeave(userId, id, request, role);
        return ResponseEntity.ok(response);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            userIdStr = request.getHeader("X-User-Id");
        }
        if (userIdStr == null) {
            throw new RuntimeException("User ID not found in request");
        }
        return Long.parseLong(userIdStr);
    }

    private String getRoleFromRequest(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (role == null) {
            role = request.getHeader("X-User-Role");
        }
        return role != null ? role : "EMPLOYEE";
    }
}

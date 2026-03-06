package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.exception.UnauthorizedException;
import com.revworkforce.admin.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DepartmentService departmentService;
    private final DesignationService designationService;
    private final HolidayService holidayService;
    private final AnnouncementService announcementService;

    public AdminController(DepartmentService departmentService,
                          DesignationService designationService,
                          HolidayService holidayService,
                          AnnouncementService announcementService) {
        this.departmentService = departmentService;
        this.designationService = designationService;
        this.holidayService = holidayService;
        this.announcementService = announcementService;
    }

    private void verifyAdminRole(String role) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Access denied. Admin role required.");
        }
    }

    // Department endpoints
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(@RequestHeader("X-User-Role") String role) {
        verifyAdminRole(role);
        return ResponseEntity.ok(departmentService.getAll());
    }

    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody DepartmentRequest request) {
        verifyAdminRole(role);
        return new ResponseEntity<>(departmentService.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {
        verifyAdminRole(role);
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Designation endpoints
    @GetMapping("/designations")
    public ResponseEntity<List<DesignationResponse>> getAllDesignations(@RequestHeader("X-User-Role") String role) {
        verifyAdminRole(role);
        return ResponseEntity.ok(designationService.getAll());
    }

    @PostMapping("/designations")
    public ResponseEntity<DesignationResponse> createDesignation(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody DesignationRequest request) {
        verifyAdminRole(role);
        return new ResponseEntity<>(designationService.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/designations/{id}")
    public ResponseEntity<Void> deleteDesignation(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {
        verifyAdminRole(role);
        designationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Holiday endpoints
    @GetMapping("/holidays")
    public ResponseEntity<List<HolidayResponse>> getAllHolidays(@RequestHeader("X-User-Role") String role) {
        verifyAdminRole(role);
        return ResponseEntity.ok(holidayService.getAll());
    }

    @GetMapping("/holidays/year/{year}")
    public ResponseEntity<List<HolidayResponse>> getHolidaysByYear(
            @RequestHeader("X-User-Role") String role,
            @PathVariable int year) {
        verifyAdminRole(role);
        return ResponseEntity.ok(holidayService.getByYear(year));
    }

    @PostMapping("/holidays")
    public ResponseEntity<HolidayResponse> createHoliday(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody HolidayRequest request) {
        verifyAdminRole(role);
        return new ResponseEntity<>(holidayService.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<Void> deleteHoliday(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {
        verifyAdminRole(role);
        holidayService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Announcement endpoints
    @GetMapping("/announcements")
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements(@RequestHeader("X-User-Role") String role) {
        verifyAdminRole(role);
        return ResponseEntity.ok(announcementService.getAll());
    }

    @PostMapping("/announcements")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody AnnouncementRequest request) {
        verifyAdminRole(role);
        return new ResponseEntity<>(announcementService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/announcements/{id}")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request) {
        verifyAdminRole(role);
        return ResponseEntity.ok(announcementService.update(id, request));
    }

    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<Void> deleteAnnouncement(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {
        verifyAdminRole(role);
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

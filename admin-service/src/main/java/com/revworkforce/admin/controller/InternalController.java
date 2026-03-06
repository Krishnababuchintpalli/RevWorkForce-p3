package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.DepartmentResponse;
import com.revworkforce.admin.dto.DesignationResponse;
import com.revworkforce.admin.dto.HolidayResponse;
import com.revworkforce.admin.service.DepartmentService;
import com.revworkforce.admin.service.DesignationService;
import com.revworkforce.admin.service.HolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal")
public class InternalController {

    private final DepartmentService departmentService;
    private final DesignationService designationService;
    private final HolidayService holidayService;

    public InternalController(DepartmentService departmentService,
                             DesignationService designationService,
                             HolidayService holidayService) {
        this.departmentService = departmentService;
        this.designationService = designationService;
        this.holidayService = holidayService;
    }

    // Department endpoints
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }

    // Designation endpoints
    @GetMapping("/designations")
    public ResponseEntity<List<DesignationResponse>> getAllDesignations() {
        return ResponseEntity.ok(designationService.getAll());
    }

    @GetMapping("/designations/{id}")
    public ResponseEntity<DesignationResponse> getDesignationById(@PathVariable Long id) {
        return ResponseEntity.ok(designationService.getById(id));
    }

    // Holiday endpoints
    @GetMapping("/holidays")
    public ResponseEntity<List<HolidayResponse>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAll());
    }

    @GetMapping("/holidays/year/{year}")
    public ResponseEntity<List<HolidayResponse>> getHolidaysByYear(@PathVariable int year) {
        return ResponseEntity.ok(holidayService.getByYear(year));
    }
}

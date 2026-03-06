package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.DepartmentRequest;
import com.revworkforce.admin.dto.DepartmentResponse;
import com.revworkforce.admin.entity.Department;
import com.revworkforce.admin.exception.DuplicateResourceException;
import com.revworkforce.admin.exception.ResourceNotFoundException;
import com.revworkforce.admin.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return toResponse(department);
    }

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department with name '" + request.getName() + "' already exists");
        }

        Department department = Department.builder()
                .name(request.getName())
                .build();

        Department saved = departmentRepository.save(department);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}

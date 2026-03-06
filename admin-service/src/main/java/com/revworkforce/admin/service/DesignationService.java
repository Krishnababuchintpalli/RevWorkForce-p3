package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.DesignationRequest;
import com.revworkforce.admin.dto.DesignationResponse;
import com.revworkforce.admin.entity.Designation;
import com.revworkforce.admin.exception.DuplicateResourceException;
import com.revworkforce.admin.exception.ResourceNotFoundException;
import com.revworkforce.admin.repository.DesignationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public List<DesignationResponse> getAll() {
        return designationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DesignationResponse getById(Long id) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + id));
        return toResponse(designation);
    }

    @Transactional
    public DesignationResponse create(DesignationRequest request) {
        if (designationRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Designation with name '" + request.getName() + "' already exists");
        }

        Designation designation = Designation.builder()
                .name(request.getName())
                .build();

        Designation saved = designationRepository.save(designation);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!designationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Designation not found with id: " + id);
        }
        designationRepository.deleteById(id);
    }

    private DesignationResponse toResponse(Designation designation) {
        return DesignationResponse.builder()
                .id(designation.getId())
                .name(designation.getName())
                .build();
    }
}

package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.AnnouncementRequest;
import com.revworkforce.admin.dto.AnnouncementResponse;
import com.revworkforce.admin.entity.Announcement;
import com.revworkforce.admin.exception.ResourceNotFoundException;
import com.revworkforce.admin.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public List<AnnouncementResponse> getAll() {
        return announcementRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AnnouncementResponse getById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
        return toResponse(announcement);
    }

    @Transactional
    public AnnouncementResponse create(AnnouncementRequest request) {
        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        Announcement saved = announcementRepository.save(announcement);
        return toResponse(saved);
    }

    @Transactional
    public AnnouncementResponse update(Long id, AnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));

        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());

        Announcement updated = announcementRepository.save(announcement);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Announcement not found with id: " + id);
        }
        announcementRepository.deleteById(id);
    }

    private AnnouncementResponse toResponse(Announcement announcement) {
        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .createdAt(announcement.getCreatedAt())
                .build();
    }
}

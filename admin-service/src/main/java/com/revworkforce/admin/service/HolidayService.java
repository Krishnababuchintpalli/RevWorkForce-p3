package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.HolidayRequest;
import com.revworkforce.admin.dto.HolidayResponse;
import com.revworkforce.admin.entity.Holiday;
import com.revworkforce.admin.exception.ResourceNotFoundException;
import com.revworkforce.admin.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public List<HolidayResponse> getAll() {
        return holidayRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<HolidayResponse> getByYear(int year) {
        return holidayRepository.findByYear(year).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HolidayResponse create(HolidayRequest request) {
        Holiday holiday = Holiday.builder()
                .name(request.getName())
                .date(request.getDate())
                .build();

        Holiday saved = holidayRepository.save(holiday);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Holiday not found with id: " + id);
        }
        holidayRepository.deleteById(id);
    }

    private HolidayResponse toResponse(Holiday holiday) {
        return HolidayResponse.builder()
                .id(holiday.getId())
                .name(holiday.getName())
                .date(holiday.getDate())
                .build();
    }
}

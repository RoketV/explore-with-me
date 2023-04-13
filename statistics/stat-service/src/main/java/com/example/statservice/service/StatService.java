package com.example.statservice.service;

import com.example.statservice.mapper.EndPointHitMapper;
import com.example.statservice.repository.StatRepository;
import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatService {

    private final StatRepository statRepository;
    private final EndPointHitMapper mapper;

    public StatService(StatRepository statRepository, EndPointHitMapper mapper) {
        this.statRepository = statRepository;
        this.mapper = mapper;
    }

    public EndPointHitDto addEndPointHit(EndPointHitDto dto) {
        log.info("hit for {} uri and {} ip added", dto.uri(), dto.ip());
        return mapper.toHitDto(statRepository.save(mapper.fromDtoToHit(dto)));
    }

    public ViewStatsDto getViewStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start,
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
        LocalDateTime endTime = LocalDateTime.parse(end,
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));

    }
}

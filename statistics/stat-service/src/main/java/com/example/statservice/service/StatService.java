package com.example.statservice.service;

import com.example.statservice.mapper.EndPointHitMapper;
import com.example.statservice.mapper.ViewStatsMapper;
import com.example.statservice.repository.StatRepository;
import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatService {

    private final StatRepository statRepository;
    private final EndPointHitMapper hitMapper;
    private final ViewStatsMapper viewStatsMapper;

    public StatService(StatRepository statRepository, EndPointHitMapper hitMapper, ViewStatsMapper viewStatsMapper) {
        this.statRepository = statRepository;
        this.hitMapper = hitMapper;
        this.viewStatsMapper = viewStatsMapper;
    }

    public EndPointHitDto addEndPointHit(EndPointHitDto dto) {
        log.info("hit for {} uri and {} ip added", dto.uri(), dto.ip());
        return hitMapper.toHitDto(statRepository.save(hitMapper.fromDtoToHit(dto)));
    }

    public List<ViewStatsDto> getViewStats(String start, String end, Optional<List<String>> uris, boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
        if (unique && uris.isPresent()) {
            log.info("view stats with unique ips collected");
            return statRepository.getViewStatsByTimeAndUrisUnique(startTime, endTime, uris.get())
                    .stream()
                    .map(viewStatsMapper::toDto)
                    .collect(Collectors.toList());
        }
        if (!unique && uris.isPresent()) {
            log.info("view stats with collected");
            return statRepository.getViewStatsByTimeAndUris(startTime, endTime, uris.get())
                    .stream()
                    .map(viewStatsMapper::toDto)
                    .collect(Collectors.toList());
        }
        if (unique) {
            return statRepository.getViewStatsByTimeAndUrisUnique(startTime, endTime)
                    .stream()
                    .map(viewStatsMapper::toDto)
                    .collect(Collectors.toList());
        }
        return statRepository.getViewStatsByTimeAndUris(startTime, endTime)
                .stream()
                .map(viewStatsMapper::toDto)
                .collect(Collectors.toList());
    }
}

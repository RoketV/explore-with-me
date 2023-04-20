package com.example.statservice.mapper;

import com.example.statservice.model.EndPointHit;
import com.explore.statdtos.dtos.EndPointHitDto;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndPointHitMapperImpl implements EndPointHitMapper {

    @Override
    public EndPointHit fromDtoToHit(EndPointHitDto dto) {
        if (dto == null) {
            return null;
        }
        return new EndPointHit(dto.id(), dto.app(), dto.uri(), dto.ip(),
                LocalDateTime.parse(URLDecoder.decode(dto.timestamp(), StandardCharsets.UTF_8),
                        DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss"))));
    }

    @Override
    public EndPointHitDto toHitDto(EndPointHit hit) {
        if (hit == null) {
            return null;
        }
        return new EndPointHitDto(hit.getId(), hit.getApp(),
                hit.getUri(), hit.getIp(), hit.getTimestamp().toString());
    }
}

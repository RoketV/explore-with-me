package com.example.statservice.mapper;

import com.example.statservice.model.EndPointHit;
import com.explore.statdtos.dtos.EndPointHitDto;
import org.springframework.stereotype.Component;

@Component
public class EndPointHitMapperImpl implements EndPointHitMapper {

    @Override
    public EndPointHit fromDtoToHit(EndPointHitDto dto) {
        if (dto == null) {
            return null;
        }
        return new EndPointHit(dto.id(), dto.app(), dto.uri(), dto.ip(), dto.timestamp());
    }

    @Override
    public EndPointHitDto toHitDto(EndPointHit hit) {
        if (hit == null) {
            return null;
        }
        return new EndPointHitDto(hit.getId(), hit.getApp(),
                hit.getUri(), hit.getIp(), hit.getTimestamp());
    }
}

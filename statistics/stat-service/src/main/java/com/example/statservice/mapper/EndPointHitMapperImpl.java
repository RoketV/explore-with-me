package com.example.statservice.mapper;

import com.example.statservice.model.EndPointHit;
import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import org.springframework.stereotype.Component;

@Component
public class EndPointHitMapperImpl implements EndPointHitMapper {
    @Override
    public ViewStatsDto toViewDto(EndPointHit hit) {
        if (hit == null) {
            return null;
        }
        return new ViewStatsDto(hit.getApp(), hit.getUri(), hit.getUri());
    }

    @Override
    public EndPointHit fromDtoToHit(EndPointHitDto dto) {
        if (dto == null) {
            return null;
        }
        return new EndPointHit(dto.id(), dto.app(), dto.uri(), dto.ip(), dto.timestamp());
    }
}

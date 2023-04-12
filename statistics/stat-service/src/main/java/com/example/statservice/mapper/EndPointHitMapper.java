package com.example.statservice.mapper;

import com.example.statservice.model.EndPointHit;
import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;

public interface EndPointHitMapper {

    ViewStatsDto toViewDto(EndPointHit hit);
    EndPointHit fromDtoToHit(EndPointHitDto dto);
}

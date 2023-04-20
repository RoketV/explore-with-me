package com.example.statservice.mapper;

import com.example.statservice.model.EndPointHit;
import com.explore.statdtos.dtos.EndPointHitDto;

public interface EndPointHitMapper {

    EndPointHit fromDtoToHit(EndPointHitDto dto);

    EndPointHitDto toHitDto(EndPointHit hit);
}

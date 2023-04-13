package com.example.statservice.mapper;

import com.example.statservice.model.ViewStats;
import com.explore.statdtos.dtos.ViewStatsDto;

public interface ViewStatsMapper {

    ViewStats fromDto(ViewStatsDto dto);
    ViewStatsDto toDto(ViewStats stats);
}

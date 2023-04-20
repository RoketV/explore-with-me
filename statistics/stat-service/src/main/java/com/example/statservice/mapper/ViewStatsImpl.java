package com.example.statservice.mapper;

import com.example.statservice.model.ViewStats;
import com.explore.statdtos.dtos.ViewStatsDto;
import org.springframework.stereotype.Component;

@Component
public class ViewStatsImpl implements ViewStatsMapper {
    @Override
    public ViewStats fromDto(ViewStatsDto dto) {
        if (dto == null) {
            return null;
        }
        return new ViewStats(dto.app(), dto.uri(), dto.hits());
    }

    @Override
    public ViewStatsDto toDto(ViewStats stats) {
        if (stats == null) {
            return null;
        }
        return new ViewStatsDto(stats.getApp(), stats.getUri(), stats.getHits());
    }
}

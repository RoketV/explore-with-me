package com.explore.statdtos.dtos;

import jakarta.validation.constraints.NotNull;

public record ViewStatsDto(
        @NotNull String app,
        @NotNull String uri,
        @NotNull Long hits) {
}

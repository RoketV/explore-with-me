package com.explore.statdtos.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record EndPointHitDto(
        Integer id,
        @NotNull String app,
        @NotNull String uri,
        @NotNull String ip,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp) {
}

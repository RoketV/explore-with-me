package com.explore.statdtos.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public record EndPointHitDto(
        Long id,
        @NotBlank(message = "app is empty or null")
        String app,
        @NotBlank(message = "uri is empty or null")
        String uri,
        @NotBlank(message = "ip is empty or null")
        String ip,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp) {
}

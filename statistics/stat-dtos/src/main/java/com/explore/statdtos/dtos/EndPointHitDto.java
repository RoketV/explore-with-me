package com.explore.statdtos.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


public class EndPointHitDto {
    private final Long id;
    @NotBlank(message = "app is empty or null")
    private final String app;
    @NotBlank(message = "uri is empty or null")
    private final String uri;
    @NotBlank(message = "ip is empty or null")
    private final String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public EndPointHitDto(Long id, String app, String uri, String ip, LocalDateTime timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public Long id() {
        return id;
    }

    public String app() {
        return app;
    }

    public String uri() {
        return uri;
    }

    public String ip() {
        return ip;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }
}

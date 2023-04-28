package com.explore.statdtos.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Jacksonized
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EndPointHitDto {
    private Long id;
    @NotBlank(message = "app is empty or null")
    private String app;
    @NotBlank(message = "uri is empty or null")
    private String uri;
    @NotBlank(message = "ip is empty or null")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;

    public EndPointHitDto(Long id, String app, String uri, String ip, String timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public EndPointHitDto(String app, String uri, String ip, String timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public EndPointHitDto() {
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

    public String timestamp() {
        return timestamp;
    }
}

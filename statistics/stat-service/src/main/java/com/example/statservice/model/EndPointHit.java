package com.example.statservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "stats", schema = "public")
public class EndPointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app")
    private String app;
    @Column(name = "uri")
    private String uri;
    @Column(name = "ip")
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public EndPointHit(Long id, String app, String uri, String ip, LocalDateTime timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public EndPointHit() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndPointHit)) return false;
        EndPointHit that = (EndPointHit) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getApp(), that.getApp()) && Objects.equals(getUri(), that.getUri()) && Objects.equals(getIp(), that.getIp()) && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getApp(), getUri(), getIp(), getTimestamp());
    }
}

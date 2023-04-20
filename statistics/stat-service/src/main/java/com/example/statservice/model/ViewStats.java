package com.example.statservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
public class ViewStats {
    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    private Long hits;

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewStats)) return false;
        ViewStats viewStats = (ViewStats) o;
        return Objects.equals(getApp(), viewStats.getApp()) && Objects.equals(getUri(), viewStats.getUri()) && Objects.equals(getHits(), viewStats.getHits());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getApp(), getUri(), getHits());
    }
}

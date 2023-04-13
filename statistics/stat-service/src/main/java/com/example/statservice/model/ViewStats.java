package com.example.statservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ViewStats {
    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    private String hits;

    public ViewStats(String app, String uri, String hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    public ViewStats() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewStats viewStats)) return false;
        return Objects.equals(app, viewStats.app) && Objects.equals(uri, viewStats.uri) && Objects.equals(hits, viewStats.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }
}

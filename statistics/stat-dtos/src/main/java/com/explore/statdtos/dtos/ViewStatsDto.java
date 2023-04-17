package com.explore.statdtos.dtos;

import jakarta.validation.constraints.NotNull;

public class ViewStatsDto{
        @NotNull
        private final String app;
        @NotNull
        private final String uri;
        @NotNull
        private final Long hits;

    public ViewStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    public String app() {
        return app;
    }

    public String uri() {
        return uri;
    }

    public Long hits() {
        return hits;
    }
}

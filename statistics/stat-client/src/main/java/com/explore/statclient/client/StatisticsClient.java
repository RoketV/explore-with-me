package com.explore.statclient.client;

import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class StatisticsClient {

    private final WebClient webClient;

    public StatisticsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public EndPointHitDto addHit(EndPointHitDto dto) {
        return webClient.post()
                .uri("/hits")
                .body(Mono.just(dto), EndPointHitDto.class)
                .retrieve()
                .bodyToMono(EndPointHitDto.class)
                .block();
    }

    public List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, boolean unique) {
        return List.of(
                Objects.requireNonNull(webClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/stats")
                                .queryParam("start", start)
                                .queryParam("end", end)
                                .queryParam("uris", uris)
                                .queryParam("unique", unique)
                                .build()
                        )
                        .retrieve()
                        .bodyToMono(ViewStatsDto[].class)
                        .block())
        );
    }
}

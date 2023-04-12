package com.explore.statclient.client;

import com.explore.statdtos.dtos.EndPointHitDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StatisticsClient {

    private final WebClient webClient;

    public StatisticsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public EndPointHitDto addHit()
}

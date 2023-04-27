package com.explore.mainservice.statistics;

import com.explore.statclient.client.StatisticsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class Config {

    private final WebClient webClient;

    @Bean
    public StatisticsClient statisticsClient() {
        return new StatisticsClient(webClient);
    }
}

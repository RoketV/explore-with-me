package com.explore.statclient.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final Integer CONNECTION_MILLIS = 5000;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_MILLIS)
                .responseTimeout(Duration.ofMillis(CONNECTION_MILLIS))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(CONNECTION_MILLIS, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(CONNECTION_MILLIS, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}

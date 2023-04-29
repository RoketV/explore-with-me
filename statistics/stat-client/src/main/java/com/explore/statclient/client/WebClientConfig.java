package com.explore.statclient.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final Integer connectionMillis = 5_000;

    @Bean
    public WebClient webClient(@Value("${stats-server.url}") String serverUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionMillis)
                .responseTimeout(Duration.ofMillis(connectionMillis))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(connectionMillis, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(connectionMillis, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(serverUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}

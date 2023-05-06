package com.explore.mainservice.statistics;

import com.explore.statclient.client.StatisticsClient;
import com.explore.statclient.client.WebClientConfig;
import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Import({StatisticsClient.class, WebClientConfig.class})
@RequiredArgsConstructor
public class StatisticService {


    private final StatisticsClient client;

    public void addHit(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndPointHitDto hitDto = new EndPointHitDto(
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter));

        client.addHit(hitDto);
    }

    public List<ViewStatsDto> getViews(String start, String end, List<String> uris, boolean unique) {

        return client.getViewStats(start, end, uris, unique);
    }
}


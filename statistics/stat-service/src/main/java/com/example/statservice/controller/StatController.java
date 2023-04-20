package com.example.statservice.controller;

import com.example.statservice.service.StatService;
import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/")
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping(value = "hit")
    public ResponseEntity<EndPointHitDto> postHit(@RequestBody EndPointHitDto dto) {
        return new ResponseEntity<>(statService.addEndPointHit(dto), HttpStatus.CREATED);
    }

    @GetMapping("stats")
    public ResponseEntity<List<ViewStatsDto>> getViewStats(@RequestParam(required = false) String start,
                                                           @RequestParam(required = false) String end,
                                                           @RequestParam(required = false) Optional<List<String>> uris,
                                                           @RequestParam(required = false) boolean unique) {
        return ResponseEntity.ok(statService.getViewStats(start, end, uris, unique));
    }
}

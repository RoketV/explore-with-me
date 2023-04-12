package com.example.statservice.controller;

import com.explore.statdtos.dtos.EndPointHitDto;
import com.explore.statdtos.dtos.ViewStatsDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatController {

    @PostMapping("/hit")
    public EndPointHitDto postHit(@RequestBody EndPointHitDto dto) {
        return null;
    }

    @GetMapping("/stats")
    public ViewStatsDto getViewStats(@RequestParam(required = false) String start,
                                     @RequestParam(required = false) String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(required = false) boolean unique) {
        return null;
    }
}

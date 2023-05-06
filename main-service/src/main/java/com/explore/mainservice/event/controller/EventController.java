package com.explore.mainservice.event.controller;

import com.explore.mainservice.event.dto.EventFullDto;
import com.explore.mainservice.event.dto.EventShortDto;
import com.explore.mainservice.event.service.EventService;
import com.explore.mainservice.statistics.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/")
public class EventController {

    private final EventService eventService;
    private final StatisticService statisticService;

    @GetMapping("events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(required = false, value = "onlyAvailable",
                                                       defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false, value = "from", defaultValue = "0")
                                               @PositiveOrZero int from,
                                               @RequestParam(required = false, value = "size", defaultValue = "10")
                                               @Positive int size,
                                               HttpServletRequest request) {

        statisticService.addHit(request);

        log.info("Получение опубликованных событий.");
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);

    }

    @GetMapping("events/{id}")
    public EventFullDto getEventPublicById(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        statisticService.addHit(request);

        log.info("Получение информации о событий с id = " + id);
        return eventService.getEventPublicById(id, request);

    }
}
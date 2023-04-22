package com.explore.mainservice.compilations.service;

import com.explore.mainservice.compilations.dto.CompilationDto;
import com.explore.mainservice.compilations.dto.NewCompilationDto;
import com.explore.mainservice.exceptions.BadRequestException;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationPersistService compilationPersistService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    public final EventPersistService eventPersistService;

    @Override
    public List<CompilationDto> getCompilation(Boolean pinned, Integer from, Integer size) {

        var compilations = compilationPersistService.findCompilation(pinned, from, size);

        if (compilations.getContent().isEmpty()) {
            return Collections.emptyList();
        }

        return compilations.getContent().stream()
                .map(el -> compilationMapper.toCompilationDto(el))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationId(Long compId) {

        var compilation = compilationPersistService.findCompilationById(compId);
        if (compilation.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Compilation with id = %s was not found", compId));
        }

        var events = compilation.get().getEvents()
                .stream()
                .map(event -> eventMapper.toEventShortDto(event,
                        categoryService.getCategoryById(event.getCategoryId()),
                        userService.getUserShortById(event.getInitiatorId()))
                ).collect(Collectors.toList());

        var compDto = compilationMapper.toCompilationDto(compilation.get());

        compDto.setEvents(events);

        return compDto;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {

        if (newCompilationDto.getTitle() == null) {
            throw new BadRequestException("Bad request body", "Compilation title is empty");
        }

        var comp = compilationPersistService.findCompilationByTitle(newCompilationDto.getTitle());

        if (comp.isPresent() && newCompilationDto.getTitle().equals(comp.get().getTitle())) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_compilation_title]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        var compilationEntity = compilationMapper.toCompilation(newCompilationDto);
        compilationEntity.setEvents(new ArrayList<>());

        newCompilationDto.getEvents().forEach(eventId ->
                compilationEntity.getEvents().add(eventPersistService.findEventById(eventId).orElseThrow(
                        () -> new NotFoundException("The required object was not found.",
                                String.format("Event with id = %s was not found", eventId)))));

        var compResult = compilationPersistService.addCompilation(compilationEntity);

        var events = compResult.getEvents()
                .stream()
                .map(event -> eventMapper.toEventShortDto(event,
                        categoryService.getCategoryById(event.getCategoryId()),
                        userService.getUserShortById(event.getInitiatorId()))
                ).collect(Collectors.toList());

        return compilationMapper.toCompilationDto(compResult);
    }

    @Override
    public void deleteCompilation(Long compId) {

        getCompilationId(compId);
        compilationPersistService.deleteCompilation(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationDto) {

        var compOpt = compilationPersistService.findCompilationById(compId);

        if (compOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Compilation with id = %s was not found", compId));
        }

        var comp = compOpt.get();

        var events = updateCompilationDto.getEvents()
                .stream()
                .map(ev -> eventPersistService.findEventById(ev).orElseThrow(
                        () -> new NotFoundException("The required object was not found.",
                                String.format("Event with id = %s was not found", ev))))
                .collect(Collectors.toList());

        comp.setEvents(events);

        var compResult = compilationPersistService.updateCompilation(comp);

        return compilationMapper.toCompilationDto(compResult);
    }
}

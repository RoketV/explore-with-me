package com.explore.mainservice.compilations.service;

import com.explore.mainservice.category.service.CategoryService;
import com.explore.mainservice.compilations.dto.CompilationDto;
import com.explore.mainservice.compilations.dto.NewCompilationDto;
import com.explore.mainservice.compilations.dto.UpdateCompilationRequestDto;
import com.explore.mainservice.compilations.jpa.CompilationPersistService;
import com.explore.mainservice.compilations.mapper.CompilationMapper;
import com.explore.mainservice.compilations.model.Compilation;
import com.explore.mainservice.event.dto.EventShortDto;
import com.explore.mainservice.event.jpa.EventPersistService;
import com.explore.mainservice.event.mapper.EventMapper;
import com.explore.mainservice.event.model.Event;
import com.explore.mainservice.exceptions.BadRequestException;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

        Page<Compilation> compilations = compilationPersistService.findCompilation(pinned, from, size);

        if (compilations.getContent().isEmpty()) {
            return Collections.emptyList();
        }

        return compilations.getContent().stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationId(Long compId) {

        Optional<Compilation> compilation = compilationPersistService.findCompilationById(compId);
        if (compilation.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Compilation with id = %s was not found", compId));
        }

        List<EventShortDto> events = compilation.get().getEvents()
                .stream()
                .map(event -> eventMapper.toEventShortDto(event,
                        categoryService.getCategoryById(event.getCategory().getId()),
                        userService.getUserShortById(event.getInitiatorId()))
                ).collect(Collectors.toList());

        CompilationDto compDto = compilationMapper.toCompilationDto(compilation.get());

        compDto.setEvents(events);

        return compDto;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {

        if (newCompilationDto.getTitle() == null) {
            throw new BadRequestException("Bad request body", "Compilation title is empty");
        }

        Optional<Compilation> comp = compilationPersistService.findCompilationByTitle(newCompilationDto.getTitle());

        if (comp.isPresent() && newCompilationDto.getTitle().equals(comp.get().getTitle())) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_compilation_title]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        Compilation compilationEntity = compilationMapper.toCompilation(newCompilationDto);
        compilationEntity.setEvents(new ArrayList<>());

        newCompilationDto.getEvents().forEach(eventId ->
                compilationEntity.getEvents().add(eventPersistService.findEventById(eventId).orElseThrow(
                        () -> new NotFoundException("The required object was not found.",
                                String.format("Event with id = %s was not found", eventId)))));

        Compilation compResult = compilationPersistService.addCompilation(compilationEntity);

        return compilationMapper.toCompilationDto(compResult);
    }

    @Override
    public void deleteCompilation(Long compId) {

        getCompilationId(compId);
        compilationPersistService.deleteCompilation(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationDto) {

        Optional<Compilation> compOpt = compilationPersistService.findCompilationById(compId);

        if (compOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Compilation with id = %s was not found", compId));
        }

        Compilation comp = compOpt.get();

        List<Event> events = updateCompilationDto.getEvents()
                .stream()
                .map(ev -> eventPersistService.findEventById(ev).orElseThrow(
                        () -> new NotFoundException("The required object was not found.",
                                String.format("Event with id = %s was not found", ev))))
                .collect(Collectors.toList());

        comp.setEvents(events);

        Compilation compResult = compilationPersistService.updateCompilation(comp);

        return compilationMapper.toCompilationDto(compResult);
    }
}

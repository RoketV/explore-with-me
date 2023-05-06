package com.explore.mainservice.compilations.service;

import com.explore.mainservice.compilations.dto.CompilationDto;
import com.explore.mainservice.compilations.dto.NewCompilationDto;
import com.explore.mainservice.compilations.dto.UpdateCompilationRequestDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilation(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationId(Long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);


    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationDto);
}

package com.explore.mainservice.compilations.mapper;

import com.explore.mainservice.compilations.dto.CompilationDto;
import com.explore.mainservice.compilations.dto.NewCompilationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    CompilationDto toCompilationDto(Compilation compilation);
}

package com.explore.mainservice.compilations.jpa;

import com.explore.mainservice.compilations.model.Compilation;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CompilationPersistService {

    Page<Compilation> findCompilation(Boolean pinned, Integer from, Integer size);

    Optional<Compilation> findCompilationById(Long compId);

    Compilation addCompilation(Compilation compilation);

    Optional<Compilation> findCompilationByTitle(String title);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(Compilation compilation);
}

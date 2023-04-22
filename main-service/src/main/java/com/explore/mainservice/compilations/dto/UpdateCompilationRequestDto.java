package com.explore.mainservice.compilations.dto;

import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UpdateCompilationRequestDto {

    private List<Long> events;

    private Boolean pinned;

    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateCompilationRequestDto)) return false;
        UpdateCompilationRequestDto that = (UpdateCompilationRequestDto) o;
        return Objects.equals(getEvents(), that.getEvents()) && Objects.equals(getPinned(), that.getPinned()) && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvents(), getPinned(), getTitle());
    }
}

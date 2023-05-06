package com.explore.mainservice.compilations.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned;

    @NotNull
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewCompilationDto)) return false;
        NewCompilationDto that = (NewCompilationDto) o;
        return Objects.equals(getEvents(), that.getEvents()) && Objects.equals(getPinned(), that.getPinned()) && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvents(), getPinned(), getTitle());
    }
}

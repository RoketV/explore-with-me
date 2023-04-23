package com.explore.mainservice.compilations.dto;

import com.explore.mainservice.event.dto.EventShortDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class CompilationDto {

    private List<EventShortDto> events;

    @NotNull
    private Long id;

    @NotNull
    private Boolean pinned;

    @NotNull
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompilationDto)) return false;
        CompilationDto that = (CompilationDto) o;
        return Objects.equals(getEvents(), that.getEvents()) && Objects.equals(getId(), that.getId()) && Objects.equals(getPinned(), that.getPinned()) && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvents(), getId(), getPinned(), getTitle());
    }
}

package com.explore.mainservice.participation.dto;

import com.explore.mainservice.participation.enums.ParticipationStatus;
import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EventRequestStatusUpdateRequestDto {

    private List<Long> requestIds;

    private ParticipationStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRequestStatusUpdateRequestDto)) return false;
        EventRequestStatusUpdateRequestDto that = (EventRequestStatusUpdateRequestDto) o;
        return Objects.equals(getRequestIds(), that.getRequestIds()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequestIds(), getStatus());
    }
}

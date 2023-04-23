package com.explore.mainservice.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ApiError {

    private List<String> errors;

    private String message;

    private String reason;

    private HttpStatus status;

    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(HttpStatus status, String reason, String message) {
        this.message = message;
        this.reason = reason;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiError)) return false;
        ApiError apiError = (ApiError) o;
        return Objects.equals(getErrors(), apiError.getErrors()) && Objects.equals(getMessage(), apiError.getMessage()) && Objects.equals(getReason(), apiError.getReason()) && getStatus() == apiError.getStatus() && Objects.equals(getTimestamp(), apiError.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrors(), getMessage(), getReason(), getStatus(), getTimestamp());
    }
}


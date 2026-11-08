package org.adelok.common.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        ErrorCode error,
        HttpStatus status,
        String message,
        String path,
        Map <String, String> details
) {
}

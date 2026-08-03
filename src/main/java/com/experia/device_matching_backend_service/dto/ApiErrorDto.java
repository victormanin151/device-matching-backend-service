package com.experia.device_matching_backend_service.dto;

import java.time.LocalDateTime;

public record ApiErrorDto (
        LocalDateTime timestamp,
        int status,
        String error,
        String message
){}

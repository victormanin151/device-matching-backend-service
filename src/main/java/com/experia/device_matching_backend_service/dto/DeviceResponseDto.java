package com.experia.device_matching_backend_service.dto;

public record DeviceResponseDto(
        String deviceId,
        long hitCount,
        String osName,
        String osVersion,
        String browserName,
        String browserVersion
) {}

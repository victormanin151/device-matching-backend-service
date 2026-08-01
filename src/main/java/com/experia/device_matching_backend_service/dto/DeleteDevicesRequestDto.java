package com.experia.device_matching_backend_service.dto;

import java.util.List;

public record DeleteDevicesRequestDto(
        List<String> deviceIds
) {
}


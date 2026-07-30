package com.experia.device_matching_backend_service.dto;

import java.time.LocalDateTime;

public record ApiInfoDto(
   String appName,
   String appVersion,
   LocalDateTime currentTime
) {}

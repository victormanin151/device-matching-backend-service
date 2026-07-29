package com.experia.device_matching_backend_service.controller.dto;

import java.time.LocalDateTime;

public record ApiInfoDto(
   String appName,
   String appVersion,
   LocalDateTime currentTime
) {}

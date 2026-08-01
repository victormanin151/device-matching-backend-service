package com.experia.device_matching_backend_service.controller;

import com.experia.device_matching_backend_service.dto.DeviceResponseDto;
import com.experia.device_matching_backend_service.service.DeviceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/match")
    public DeviceResponseDto matchDevice(
            @RequestHeader("User-Agent") String userAgent
    ) {
        return deviceService.matchDevice(userAgent);
    }
}

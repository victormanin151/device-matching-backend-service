package com.experia.device_matching_backend_service.controller;

import com.experia.device_matching_backend_service.dto.DeviceResponseDto;
import com.experia.device_matching_backend_service.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
    public DeviceResponseDto getDeviceById(@PathVariable("id") String Id){
        return deviceService.findDeviceById(Id);
    }

    @PostMapping("/match")
    public DeviceResponseDto matchDevice(
            @RequestHeader("User-Agent") String userAgent
    ) {
        return deviceService.matchDevice(userAgent);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceById(@PathVariable("id") String id) {
        deviceService.deleteDevice(id);
    }
}

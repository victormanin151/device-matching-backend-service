package com.experia.device_matching_backend_service.dto;

import com.experia.device_matching_backend_service.model.Device;

public record DeviceResponseDto(
        String deviceId,
        long hitCount,
        String osName,
        String osVersion,
        String browserName,
        String browserVersion
) {
    public static DeviceResponseDto fromEntity(Device device){
        return new DeviceResponseDto(
                device.getDeviceId(),
                device.getHitCount(),
                device.getOsName(),
                device.getOsVersion(),
                device.getBrowserName(),
                device.getBrowserVersion()
        );
    }
}

package com.experia.device_matching_backend_service.service;

import com.experia.device_matching_backend_service.dto.DeviceResponseDto;
import com.experia.device_matching_backend_service.model.Device;
import com.experia.device_matching_backend_service.parser.ParsedUserAgent;
import com.experia.device_matching_backend_service.parser.UserAgentParser;
import com.experia.device_matching_backend_service.repository.DeviceRepository;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserAgentParser userAgentParser;

    public DeviceService(DeviceRepository deviceRepository, UserAgentParser userAgentParser) {
        this.deviceRepository = deviceRepository;
        this.userAgentParser = userAgentParser;
    }

    public DeviceResponseDto matchDevice(String userAgent){

        ParsedUserAgent parsed = userAgentParser.parse(userAgent);

        Optional<Device> existingDevice =
                deviceRepository
                        .findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(
                                QueryParam.of(parsed.osName()),
                                QueryParam.of(parsed.osVersion()),
                                QueryParam.of(parsed.browserName()),
                                QueryParam.of(parsed.browserVersion())
                        );

        Device device;

        if(existingDevice.isPresent()){
            device = existingDevice.get();
            device.incrementHitCount();
        } else{
            device = new Device(
                    UUID.randomUUID().toString(),
                    1L,
                    parsed.osName(),
                    parsed.osVersion(),
                    parsed.browserName(),
                    parsed.browserVersion());
        }
        device = deviceRepository.save(device);

        return new DeviceResponseDto(
                device.getDeviceId(),
                device.getHitCount(),
                device.getOsName(),
                device.getOsVersion(),
                device.getBrowserName(),
                device.getBrowserVersion()
        );
    }

    public DeviceResponseDto findDeviceById(String id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No device with that ID"));
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
package com.experia.device_matching_backend_service.service;

import com.experia.device_matching_backend_service.dto.DeviceResponseDto;
import com.experia.device_matching_backend_service.model.Device;
import com.experia.device_matching_backend_service.parser.ParsedUserAgent;
import com.experia.device_matching_backend_service.parser.UserAgentParser;
import com.experia.device_matching_backend_service.repository.DeviceRepository;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.stereotype.Service;

import java.util.*;

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

        return DeviceResponseDto.fromEntity(device);
    }

    public DeviceResponseDto findDeviceById(String id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No device with that ID"));
        //fix later to return proper Status code (right now 500)
        return DeviceResponseDto.fromEntity(device);
    }

    public void deleteDeviceById(String id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No device with that ID"));
        deviceRepository.delete(device);
    }

    public void deleteDevices(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one device ID must be provided."
            );
        }

        Set<String> uniqueIds = new HashSet<>(ids);

        if (uniqueIds.size() != ids.size()) {
            throw new IllegalArgumentException(
                    "Duplicate device IDs are not allowed."
            );
        }

        Iterable<Device> foundDevices =
                deviceRepository.findAllById(ids);

        List<Device> devices = new ArrayList<>();
        foundDevices.forEach(devices::add);

        if (ids.size() != devices.size()) {
            throw new IllegalArgumentException(
                    "One or more device IDs were not found."
            );
        }

        deviceRepository.deleteAllById(ids);
    }

    public List<DeviceResponseDto> findByOsName (String osName){

        if (osName == null || osName.isBlank()) {
            throw new IllegalArgumentException(
                    "osName is empty or null."
            );
        }

        List<Device> devices = deviceRepository.findByOsName(osName);

        return devices.stream()
                .map(DeviceResponseDto::fromEntity)
                .toList();
    }
}
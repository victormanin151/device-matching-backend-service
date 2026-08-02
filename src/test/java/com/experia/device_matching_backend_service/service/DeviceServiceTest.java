package com.experia.device_matching_backend_service.service;

import com.experia.device_matching_backend_service.dto.DeviceResponseDto;
import com.experia.device_matching_backend_service.model.Device;
import com.experia.device_matching_backend_service.parser.ParsedUserAgent;
import com.experia.device_matching_backend_service.parser.UserAgentParser;
import com.experia.device_matching_backend_service.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.aerospike.query.QueryParam;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private UserAgentParser userAgentParser;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void shouldIncrementHitCountWhenDeviceAlreadyExists() {
        ParsedUserAgent parsedUserAgent = new ParsedUserAgent(
                "Windows NT",
                "10.0",
                "Chrome",
                "120"
        );

        Device existingDevice = new Device(
                "device-123",
                5L,
                "Windows NT",
                "10.0",
                "Chrome",
                "120"
        );

        String userAgent = "test-user-agent";

        when(userAgentParser.parse(userAgent)).thenReturn(parsedUserAgent);

        when(deviceRepository.findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(
                any(QueryParam.class),
                any(QueryParam.class),
                any(QueryParam.class),
                any(QueryParam.class)
                )).thenReturn(Optional.of(existingDevice));

        when(deviceRepository.save(existingDevice))
                .thenReturn(existingDevice);

        DeviceResponseDto result = deviceService.matchDevice(userAgent);

        assertEquals(6L, result.hitCount());
        assertEquals("device-123", result.deviceId());

        verify(deviceRepository).save(existingDevice);
    }

}

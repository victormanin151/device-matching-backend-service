package com.experia.device_matching_backend_service.service;

import com.experia.device_matching_backend_service.dto.DeviceResponseDto;
import com.experia.device_matching_backend_service.model.Device;
import com.experia.device_matching_backend_service.parser.ParsedUserAgent;
import com.experia.device_matching_backend_service.parser.UserAgentParser;
import com.experia.device_matching_backend_service.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.aerospike.query.QueryParam;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldCreateNewDeviceWhenDeviceDoesNotExist() {
        ParsedUserAgent parsedUserAgent = new ParsedUserAgent(
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
        )).thenReturn(Optional.empty());

        when(deviceRepository.save(any(Device.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeviceResponseDto result = deviceService.matchDevice(userAgent);

        ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);

        verify(deviceRepository).save(captor.capture());

        Device savedDevice = captor.getValue();

        assertEquals(1L, savedDevice.getHitCount());
        assertEquals("Windows NT", savedDevice.getOsName());
        assertEquals("10.0", savedDevice.getOsVersion());
        assertEquals("Chrome", savedDevice.getBrowserName());
        assertEquals("120", savedDevice.getBrowserVersion());
        assertNotNull(savedDevice.getDeviceId());

        assertEquals(1L, result.hitCount());
        assertNotNull(result.deviceId());

        assertEquals(savedDevice.getDeviceId(), result.deviceId());
    }

    @Test
    void shouldReturnDeviceById(){
        String id = "device-123";

        Device device = new Device(
                id,
                5L,
                "Windows NT",
                "10.0",
                "Chrome",
                "120"
        );

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        DeviceResponseDto result =  deviceService.findDeviceById(id);

        verify(deviceRepository).findById(id);

        assertEquals(id,result.deviceId());
        assertEquals(5L,result.hitCount());
        assertEquals("Windows NT",result.osName());
        assertEquals("10.0",result.osVersion());
        assertEquals("Chrome",result.browserName());
        assertEquals("120",result.browserVersion());
    }

    @Test
    void shouldThrowWhenDeviceIdDoesNotExist(){
        String id = "device-123";

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> deviceService.findDeviceById(id)
        );

        assertEquals("No device with that ID", exception.getMessage());
    }

    @Test
    void shouldReturnDevicesWithMatchingOsName(){
        String osName = "Windows NT";

        Device firstDevice = new Device(
                "device-1",
                2L,
                "Windows NT",
                "10.0",
                "Chrome",
                "120"
        );

        Device secondDevice = new Device(
                "device-2",
                4L,
                "Windows NT",
                "10.0",
                "Firefox",
                "121"
        );

        List<Device> devices = List.of(firstDevice, secondDevice);

        when(deviceRepository.findByOsName(osName)).thenReturn(devices);

        List<DeviceResponseDto> result =
                deviceService.findByOsName(osName);

        assertEquals(2,result.size());
        assertEquals("device-1", result.get(0).deviceId());
        assertEquals("Chrome", result.get(0).browserName());
        assertEquals("device-2", result.get(1).deviceId());
        assertEquals("Firefox", result.get(1).browserName());

        verify(deviceRepository).findByOsName(osName);
    }

    @Test
    void shouldReturnEmptyListWhenNoDevicesMatchOsName(){
        String osName = "Windows NT";

        when(deviceRepository.findByOsName(osName)).thenReturn(List.of());

        List<DeviceResponseDto> result =
                deviceService.findByOsName(osName);

        assertTrue(result.isEmpty());

    }

    @Test
    void shouldThrowWhenOsNameIsNullOrEmpty(){
        String osName = "";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deviceService.findByOsName(osName)
        );

        assertEquals("osName is empty or null.", exception.getMessage());
    }

    @Test
    void shouldDeleteDeviceWhenIdExists(){
        String id = "device-123";

        Device device = new Device(
                id,
                5L,
                "Windows NT",
                "10.0",
                "Chrome",
                "120"
        );

        when(deviceRepository.findById(id))
                .thenReturn(Optional.of(device));

        deviceService.deleteDeviceById(id);

        verify(deviceRepository).findById(id);
        verify(deviceRepository).delete(device);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingDevice(){
        String id = "device-123";

        when(deviceRepository.findById(id))
                .thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> deviceService.deleteDeviceById(id)
        );

        assertEquals("No device with that ID", exception.getMessage());

        verify(deviceRepository, never()).delete(any(Device.class));
    }

    @Test
    void shouldDeleteMultipleDevices(){

        List<String> ids = List.of("device-1", "device-2");

        Device firstDevice = new Device(
                "device-1",
                2L,
                "Windows NT",
                "10.0",
                "Chrome",
                "120"
        );

        Device secondDevice = new Device(
                "device-2",
                4L,
                "Windows NT",
                "10.0",
                "Firefox",
                "121"
        );

        List<Device> devices = List.of(firstDevice,secondDevice);

        when(deviceRepository.findAllById(ids))
                .thenReturn(devices);

        deviceService.deleteDevices(ids);

        verify(deviceRepository).findAllById(ids);
        verify(deviceRepository).deleteAllById(ids);
    }

    @Test
    void shouldThrowWhenDeviceIdsAreNull(){
        List<String> ids = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deviceService.deleteDevices(ids)
        );

        assertEquals("At least one device ID must be provided.", exception.getMessage());
    }

    @Test
    void shouldThrowWhenDeviceIdsAreEmpty(){
        List<String> ids = List.of();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deviceService.deleteDevices(ids)
        );

        assertEquals("At least one device ID must be provided.", exception.getMessage());
    }

    @Test
    void shouldThrowWhenDuplicateDeviceIdsAreProvided(){
        List<String> ids = List.of("device-1", "device-1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deviceService.deleteDevices(ids)
        );

        assertEquals("Duplicate device IDs are not allowed.", exception.getMessage());
    }
}

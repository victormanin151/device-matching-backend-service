package com.experia.device_matching_backend_service.model;

import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class Device {

    @Id
    private String deviceId;

    private long hitCount;

    private String osName;

    private String osVersion;

    private String browserName;

    private String browserVersion;

    public Device() {
    }

    public Device(String deviceId, long hitCount, String osName, String osVersion, String browserName, String browserVersion) {
        this.deviceId = deviceId;
        this.hitCount = hitCount;
        this.osName = osName;
        this.osVersion = osVersion;
        this.browserName = browserName;
        this.browserVersion = browserVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getHitCount() {
        return hitCount;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getOsName() {
        return osName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void incrementHitCount() {
        this.hitCount++;
    }
}

package com.experia.device_matching_backend_service.parser;

public record ParsedUserAgent(
        String osName,
        String osVersion,
        String browserName,
        String browserVersion
) {}

package com.experia.device_matching_backend_service.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAgentParserTest {

    private final UserAgentParser parser = new UserAgentParser();


    @Test
    void shouldParseChromeOnWindows(){
        String userAgent =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/120.0.0.0 Safari/537.36";

        ParsedUserAgent result = parser.parse(userAgent);
        assertEquals("Windows NT", result.osName());
 //       assertEquals("10", result.osVersion());
        assertEquals("Chrome", result.browserName());
        assertEquals("120", result.browserVersion());
    }
}

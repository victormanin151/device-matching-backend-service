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
        assertEquals("10.0", result.osVersion());
        assertEquals("Chrome", result.browserName());
        assertEquals("120", result.browserVersion());
    }

    @Test
    void shouldParseChromeOnLinux() {

        String userAgent =
                "Mozilla/5.0 (X11; Linux x86_64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Safari/537.36";

        ParsedUserAgent result = parser.parse(userAgent);

        assertEquals("Linux", result.osName());
        assertEquals("??", result.osVersion());
        assertEquals("Chrome", result.browserName());
        assertEquals("120", result.browserVersion());
    }

    @Test
    void shouldParseSafariOnMacOs() {

        String userAgent =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) " +
                        "Version/17.0 Safari/605.1.15";

        ParsedUserAgent result = parser.parse(userAgent);

        assertEquals("Mac OS", result.osName());
        assertEquals(">=10.15.7", result.osVersion());
        assertEquals("Safari", result.browserName());
        assertEquals("17.0", result.browserVersion());
    }

    @Test
    void shouldParseChromeOnAndroid() {

        String userAgent =
                "Mozilla/5.0 (Linux; Android 14; Pixel 8) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Mobile Safari/537.36";

        ParsedUserAgent result = parser.parse(userAgent);

        assertEquals("Android", result.osName());
        assertEquals("14", result.osVersion());
        assertEquals("Chrome", result.browserName());
        assertEquals("120", result.browserVersion());
    }
}

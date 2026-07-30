package com.experia.device_matching_backend_service.parser;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

@Component
public class UserAgentParser {

    private final UserAgentAnalyzer analyzer;

    public UserAgentParser() {
        this.analyzer = UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(10_000)
                .withField(UserAgent.OPERATING_SYSTEM_NAME)
                .withField(UserAgent.OPERATING_SYSTEM_VERSION)
                .withField(UserAgent.AGENT_NAME)
                .withField(UserAgent.AGENT_VERSION)
                .build();
    }

    public ParsedUserAgent parse(String userAgentString){
        UserAgent userAgentAnalyzer = analyzer.parse(userAgentString);

        String osName = userAgentAnalyzer.getValue(UserAgent.OPERATING_SYSTEM_NAME);

        String osVersion = userAgentAnalyzer.getValue(UserAgent.OPERATING_SYSTEM_VERSION);

        String browserName = userAgentAnalyzer.getValue(UserAgent.AGENT_NAME);

        String browserVersion = userAgentAnalyzer.getValue(UserAgent.AGENT_VERSION);

        return new ParsedUserAgent(
                osName,
                osVersion,
                browserName,
                browserVersion
        );
    }

}

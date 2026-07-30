package com.experia.device_matching_backend_service.controller;


import com.experia.device_matching_backend_service.dto.ApiInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    public ApiInfoDto ApiInfo(){
        LocalDateTime currentTime = LocalDateTime.now();
        return new ApiInfoDto(appName, appVersion, currentTime);
    }
}

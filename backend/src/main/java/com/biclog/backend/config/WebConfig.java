package com.biclog.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 요청이 오면 실제 uploads 폴더의 파일을 제공
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
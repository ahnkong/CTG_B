package com.ctg.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://192.168.0.7:3000")
                        // .allowedOrigins("http://localhost:3000", "http://192.168.0.39:3000", "http://http://168.126.63.2:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS, PATCH")
                        // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS)
                        .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                        .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Set-Cookie")
                        .allowCredentials(true);
            }
        };
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter)
                        .setSupportedMediaTypes(Arrays.asList(
                                MediaType.APPLICATION_JSON,
                                MediaType.APPLICATION_JSON_UTF8 // charset=UTF-8 지원 추가
                        ));
            }
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "uploads" 경로를 실제 디렉토리로 매핑
        registry.addResourceHandler("/uploads/**")
                // .addResourceLocations("file:/Users/jieunseo/uploads/"); // 실제 파일 저장 경로_서즌
                .addResourceLocations("file:/Users/ahncoco/uploads/"); // 실제 파일 저장 경로_안코코
                // .addResourceLocations("file:/Users/hylee/uploads/"); // 실제 파일 저장 경로_나카이마
    }

}

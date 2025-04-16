package com.ftohbackend.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SomeConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/").allowedOrigins("http://localhost:3000").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//    }
	
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/")  // Changed from "/" to "/**" to apply to all endpoints
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
         }
}



package com.ftohbackend.config;


import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.api.key}")
    private String apiKey;

    @Value("${cloudinary.api.secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );
        return new Cloudinary(config);
    }
    
    
//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//            "cloud_name", your_cloud_name,
//            "api_key", your_api_key,
//            "api_secret", your_api_secret,
//            "secure", true));
//    }
}


//dqarxeygt
//995583833121231
//dL2BK4p1qKd2odkjZGtyjzlpv_c
//CLOUDINARY_URL=cloudinary://995583833121231:dL2BK4p1qKd2odkjZGtyjzlpv_c@dqarxeygt
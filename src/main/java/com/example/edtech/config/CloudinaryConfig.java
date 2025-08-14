package com.example.edtech.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhtgh8ktx",
                "api_key", "667835323942981",
                "api_secret", "zHkp4zICqyw_7PJE54YkqqekBPc"
        ));
    }
}

package com.example.edtech.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class VideoUploadService {

    private final Cloudinary cloudinary;

    public String uploadVideo(MultipartFile file)throws IOException {
        Map uploadResult = cloudinary.uploader().uploadLarge(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "lectures"
        ));

        return uploadResult.get("secure_url").toString();
    }

    public Map<String,String>uploadVideoWithResolution(MultipartFile file)throws  IOException{
        String originalUrl=uploadVideo(file);
        return  Map.of("original",originalUrl,
                "720p",originalUrl+"?transformation=...720p...",
                "480p",originalUrl+"?transformation=...480p..."
                );
    }
}

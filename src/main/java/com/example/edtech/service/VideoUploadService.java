package com.example.edtech.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.edtech.util.CloudinaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class VideoUploadService {

    private final Cloudinary cloudinary;

    public CloudinaryResponse uploadVideo(MultipartFile file)throws IOException {
        Map uploadResult = cloudinary.uploader().uploadLarge(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "lectures"
        ));
     String url=   uploadResult.get("secure_url").toString();
       String id= uploadResult.get("public_id").toString();
        CloudinaryResponse response=new CloudinaryResponse(url,id);
   return  response;
    }

    public Map<String,String>uploadVideoWithResolution(MultipartFile file)throws  IOException{
        CloudinaryResponse response = uploadVideo(file);
        String originalUrl=response.getUrl();
        String id=response.getId();
        return  Map.of("url",originalUrl,
                "720p",originalUrl+"?transformation=...720p...",
                "480p",originalUrl+"?transformation=...480p...",
                "id",id
                );
    }

    public void deleteFile(String id) {
        try {
            cloudinary.uploader().destroy(id, ObjectUtils.asMap(
                    "resource_type", "video"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }

}


package com.example.edtech.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.edtech.util.CloudinaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

@Value("${cloudinary.profiles}")
    private String profileBucket;




    public CloudinaryResponse uploadFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        String bucketName = null;
        try{
            bucketName = profileBucket;

            // Remove extension if present (optional, but cleaner)
            String publicId = originalFileName != null
                    ? originalFileName.replaceFirst("[.][^.]+$", "")
                    : "default_filename";

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", bucketName,
                    "public_id", publicId
            ));
            String secureUrl = uploadResult.get("secure_url").toString();
            String Id = uploadResult.get("public_id").toString();
            return new CloudinaryResponse(Id,secureUrl);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }

        // Determine the bucket based on the bucket type



    }


    public void deleteFile(String id) {
        try {
            cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

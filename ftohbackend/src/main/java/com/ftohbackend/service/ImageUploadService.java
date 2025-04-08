package com.ftohbackend.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "vehicle"));
    }

    public void deleteImage(String imageUrl) throws IOException {

        String publicId = extractPublicId(imageUrl);

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    private String extractPublicId(String imageUrl) {

        String[] parts = imageUrl.split("/");
        String publicId = parts[parts.length - 1];


        publicId = publicId.replaceAll("^(v\\d+/)?", "");
        publicId = publicId.substring(0, publicId.lastIndexOf("."));

        return publicId;
    }

}



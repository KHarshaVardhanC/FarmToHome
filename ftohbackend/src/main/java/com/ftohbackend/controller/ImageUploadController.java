package com.ftohbackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.service.ImageUploadService;

@RestController
@RequestMapping("/image")
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
//        try {
//            Map uploadResult = imageUploadService.uploadImage(file);
//            return ResponseEntity.ok(uploadResult);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
//        }
//    }
}
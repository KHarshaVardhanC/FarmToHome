package com.ftohbackend.service;


import org.springframework.stereotype.Service;

@Service
public class ImageUploadService {

//    @Autowired
//    private Cloudinary cloudinary;
//
//    public Map uploadImage(MultipartFile file) throws IOException {
//        return cloudinary.uploader().upload(file.getBytes(),
//                ObjectUtils.asMap("folder", "vehicle"));
//    }
//
//    public void deleteImage(String imageUrl) throws IOException {
//
//        String publicId = extractPublicId(imageUrl);
//
//        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//    }
//
//    private String extractPublicId(String imageUrl) {
//
//        String[] parts = imageUrl.split("/");
//        String publicId = parts[parts.length - 1];
//
//
//        publicId = publicId.replaceAll("^(v\\d+/)?", "");
//        publicId = publicId.substring(0, publicId.lastIndexOf("."));
//
//        return publicId;
//    }
	
	
	
	
//	private String uploadImage(MultipartFile file) throws IOException {
//	    try {
//	        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//	        return uploadResult.get("url").toString();
//	    } catch (IOException e) {
//	        throw new IOException("Failed to upload image to Cloudinary", e);
//	    }
//	}
	
	

}



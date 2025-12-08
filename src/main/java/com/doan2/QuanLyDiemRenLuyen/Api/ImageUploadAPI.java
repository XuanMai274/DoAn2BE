package com.doan2.QuanLyDiemRenLuyen.Api;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/public/images")
public class ImageUploadAPI {
    private final Cloudinary cloudinary;

    public ImageUploadAPI(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile file){
        try {
            String originalFilename = file.getOriginalFilename();
            String resourceType = "auto"; // mặc định

            if (originalFilename != null) {
                String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
                if (ext.matches("jpg|jpeg|png|gif|bmp|webp")) {
                    resourceType = "image";
                } else if (ext.matches("mp4|mov|avi|mkv")) {
                    resourceType = "video";
                } else {
                    resourceType = "raw"; // pdf, docx, zip, v.v.
                }
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "QuanLyDiemRenLuyen_files",
                            "resource_type", resourceType
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();

            return ResponseEntity.ok(Map.of(
                    "message", "Upload thành công",
                    "url", fileUrl,
                    "resource_type", resourceType,
                    "public_id", uploadResult.get("public_id")
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
//        try {
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                    ObjectUtils.asMap(
//                            "folder", "QuanLyDiemRenLuyen_images",    // tạo folder riêng
//                            "resource_type", "auto"       // tự nhận dạng ảnh/video
//                    ));
//            String imageUrl = uploadResult.get("secure_url").toString();
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "Upload thành công",
//                    "url", imageUrl,
//                    "public_id", uploadResult.get("public_id")
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("error: ", e.getMessage()));
//        }
    }
    // DELETE API — xóa ảnh bằng public_id
    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<?> deleteImage(@PathVariable("publicId") String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Cloudinary trả về {"result": "ok"} nếu xóa thành công
            if ("ok".equals(result.get("result"))) {
                return ResponseEntity.ok(Map.of(
                        "message", "Xóa ảnh thành công",
                        "public_id", publicId
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Không tìm thấy hoặc xóa thất bại",
                        "public_id", publicId,
                        "cloudinary_response", result
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

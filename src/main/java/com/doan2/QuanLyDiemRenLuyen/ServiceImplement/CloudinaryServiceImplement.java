package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doan2.QuanLyDiemRenLuyen.Service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImplement implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImplement(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    @Override
    public Map<String, String> uploadImage(MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        if (file == null || file.isEmpty()) {
            response.put("Error", "File rỗng");
            return response;
        }

        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) originalFilename = "file";
            // tách tên & extension
            int idx = originalFilename.lastIndexOf('.');
            String baseName = (idx > 0) ? originalFilename.substring(0, idx) : originalFilename;
            String extension = (idx > 0) ? originalFilename.substring(idx + 1).toLowerCase() : "";

            // timestamp để tránh trùng tên
            String ts = String.valueOf(System.currentTimeMillis());

            // danh sách extension ảnh
            List<String> imageExt = Arrays.asList("jpg","jpeg","png","gif","webp","bmp","tif","tiff","svg");
            String folderImage = "QuanLyDiemRenLuyen_images";
            String folderFile = "QuanLyDiemRenLuyen_files";

            Map<String, Object> uploadOptions;
            Map<String, Object> uploadResult;
            String fileUrl, publicId;

            if (imageExt.contains(extension)) {
                // upload ảnh
                // public_id thường không cần extension cho image, nhưng bạn vẫn có thể đặt
                publicId = baseName + "_" + ts; // Cloudinary tự thêm resource type / version
                uploadOptions = ObjectUtils.asMap(
                        "folder", folderImage,
                        "resource_type", "image",
                        "public_id", publicId,
                        "use_filename", false,
                        "unique_filename", false,
                        "overwrite", false
                );
                uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
                fileUrl = uploadResult.get("secure_url").toString();
                response.put("type", "image");
            } else {
                // upload file (pdf/docx/...)
                // IMPORTANT: public_id _must_ include extension để URL có đuôi .pdf/.docx
                if (extension.isEmpty()) {
                    // nếu không có extension, trả lỗi hoặc ép đặt .bin
                    extension = "bin";
                }
                publicId = baseName + "_" + ts + "." + extension; // giữ extension trong public_id
                uploadOptions = ObjectUtils.asMap(
                        "folder", folderFile,
                        "resource_type", "raw",
                        "public_id", publicId,
                        "use_filename", false,
                        "unique_filename", false,
                        "overwrite", false
                );
                uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
                fileUrl = uploadResult.get("secure_url").toString();
                response.put("type", "raw");
            }

            response.put("url", fileUrl);
            response.put("public_id", (String) uploadResult.get("public_id"));
            response.put("original_filename", originalFilename);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Upload không thành công: " + e.getMessage());
            return response;
        }
    }
//    @Override
//    public Map<String, String> uploadImage(MultipartFile file) {
//        Map<String, String> respone= new HashMap<>();
//        try {
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                    ObjectUtils.asMap(
//                            "folder", "QuanLyDiemRenLuyen_images",    // tạo folder riêng
//                            "resource_type", "auto"
//                    ));
//            String imageUrl = uploadResult.get("secure_url").toString();
//            respone.put("url",imageUrl);
//            respone.put("public_id", (String) uploadResult.get("public_id"));
//            return respone;
//        } catch (Exception e) {
//            respone.put("Error","Upload không thành công");
//            System.out.println(e.getMessage());
//            return respone;
//        }
//    }

    @Override
    public String deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Cloudinary trả về {"result": "ok"} nếu xóa thành công
            if ("ok".equals(result.get("result"))) {
                return "Xóa ảnh thành công";
            } else {
                return "Xóa ảnh không thành công";
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

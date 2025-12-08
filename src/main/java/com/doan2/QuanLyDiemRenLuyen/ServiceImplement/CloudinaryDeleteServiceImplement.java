package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doan2.QuanLyDiemRenLuyen.Service.CloudinaryDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryDeleteServiceImplement implements CloudinaryDeleteService {
    @Autowired
    private Cloudinary cloudinary;
    /**
     * Xóa ảnh theo public_id lưu trong DB
     * @param publicId public_id của ảnh trên Cloudinary
     * @return true nếu xóa thành công, false nếu thất bại
     */
    @Override
    public boolean deleteImage(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

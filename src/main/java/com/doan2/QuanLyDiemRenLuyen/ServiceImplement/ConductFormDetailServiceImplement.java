package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.Entity.ConductFormDetailEntity;
import com.doan2.QuanLyDiemRenLuyen.Repository.ConductFormDetailRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.ConductFormRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.CloudinaryDeleteService;
import com.doan2.QuanLyDiemRenLuyen.Service.ConductFormDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConductFormDetailServiceImplement implements ConductFormDetailService {
    @Autowired
    ConductFormDetailRepository conductFormDetailRepository;
    @Autowired
    CloudinaryDeleteService cloudinaryDeleteService;
    @Override
    public Boolean deleteImageFormConductFormDetail(int idConductFormDetail) {
        // lay lên đối tượng ConductFormDetail từ db
        ConductFormDetailEntity conductFormDetailEntity=conductFormDetailRepository.findByConductFormDetailId(idConductFormDetail);
        if(conductFormDetailEntity!=null){
            // Lấy giá trị của public_id tai cột đó
            boolean statusDelete=cloudinaryDeleteService.deleteImage(conductFormDetailEntity.getPublic_id());
            // nếu xóa trên cloud thành công thì tiến hành xóa trong cơ sở dữ liệu

        }
        return null;
    }
}

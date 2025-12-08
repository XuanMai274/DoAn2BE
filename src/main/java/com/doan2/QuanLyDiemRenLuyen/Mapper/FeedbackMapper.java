package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.*;
import com.doan2.QuanLyDiemRenLuyen.Entity.ConductFormEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.FeedbackEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    // ============================
    // ENTITY → DTO
    // ============================
    public FeedbackDTO toDTO(FeedbackEntity entity) {
        if (entity == null) return null;

        FeedbackDTO dto = new FeedbackDTO();
        dto.setFeedbackId(entity.getFeedbackId());
        dto.setContent(entity.getContent());
        dto.setImage(entity.getImage());
        dto.setGmail(entity.getGmail());
        dto.setCreateAt(entity.getCreateAt());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setResponseContent(entity.getResponseContent());
        dto.setResponse(entity.isResponse());
        // ----- Student (chỉ map các field cần thiết) -----
        if (entity.getStudentEntity() != null) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setStudentId(entity.getStudentEntity().getStudentId());
            studentDTO.setFullname(entity.getStudentEntity().getFullname());
            dto.setStudentDTO(studentDTO);
        }

        // ----- Manager (chỉ map mã & tên) -----
        if (entity.getManagerEntity() != null) {
            ManagerDTO managerDTO = new ManagerDTO();
            managerDTO.setManagerId(entity.getManagerEntity().getManagerId());
            managerDTO.setFullname(entity.getManagerEntity().getFullname());
            dto.setManagerDTO(managerDTO);
        }

        // ----- ConductForm (mã phiếu + học kỳ + năm học) -----
        if (entity.getConductFormEntity() != null) {
            ConductFormDTO conductDTO = new ConductFormDTO();
            conductDTO.setConductFormId(entity.getConductFormEntity().getConductFormId());

            if (entity.getConductFormEntity().getSemesterEntity() != null) {
                SemesterDTO sem = new SemesterDTO();
                sem.setSemesterName(entity.getConductFormEntity().getSemesterEntity().getSemesterName());
                sem.setYear(entity.getConductFormEntity().getSemesterEntity().getYear());
                conductDTO.setSemester(sem);
            }

            dto.setConductFormDTO(conductDTO);
        }

        return dto;
    }

    // ============================
    // DTO → ENTITY
    // (chỉ map ID đối tượng liên kết)
    // ============================
    public FeedbackEntity toEntity(FeedbackDTO dto) {

        if (dto == null) return null;

        FeedbackEntity entity = new FeedbackEntity();
        entity.setFeedbackId(dto.getFeedbackId());
        entity.setContent(dto.getContent());
        entity.setImage(dto.getImage());
        entity.setGmail(dto.getGmail());
        entity.setResponseContent(dto.getResponseContent());
        return entity;
    }
}

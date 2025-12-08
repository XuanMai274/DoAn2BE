package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.*;
import com.doan2.QuanLyDiemRenLuyen.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConductFormMapper {
    @Autowired ClassMapper classMapper;
    public ConductFormDTO toDTO(ConductFormEntity entity) {
        if (entity == null) return null;

        ConductFormDTO dto = new ConductFormDTO();
        dto.setConductFormId(entity.getConductFormId());
        dto.setTotalStudentScore(entity.getTotalStudentScore());
        dto.setClassMonitorScore(entity.getClassMonitorScore());
        dto.setStaffScore(entity.getStaff_score());
        dto.setStatus(entity.getStatus());
        dto.setCreateAt(entity.getCreateAt());
        dto.setUpdatedDate(entity.getUpdatedDate());

        // Map các quan hệ (chỉ lấy thông tin cần thiết để tránh vòng lặp vô hạn)
        if (entity.getSemesterEntity() != null) {
            SemesterDTO semesterDTO = new SemesterDTO();
            semesterDTO.setSemesterId(entity.getSemesterEntity().getSemesterId());
            semesterDTO.setSemesterName(entity.getSemesterEntity().getSemesterName());
            semesterDTO.setYear(entity.getSemesterEntity().getYear());
            dto.setSemester(semesterDTO);
        }

        if (entity.getStudentEntity() != null) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setStudentId(entity.getStudentEntity().getStudentId());
            studentDTO.setFullname(entity.getStudentEntity().getFullname());
            studentDTO.setClassDTO(classMapper.toDTO(entity.getStudentEntity().getClassId()));
            dto.setStudent(studentDTO);
        }

        if (entity.getConductFormDetailEntityList() != null) {
            dto.setConductFormDetailList(
                    entity.getConductFormDetailEntityList()
                            .stream()
                            .map(this::toDetailDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
    public ConductFormEntity toEntity(ConductFormDTO dto) {
        if (dto == null) return null;
        ConductFormEntity entity = new ConductFormEntity();
        entity.setConductFormId(dto.getConductFormId());
        entity.setTotalStudentScore(dto.getTotalStudentScore());
        entity.setClassMonitorScore(dto.getClassMonitorScore());
        entity.setStaff_score(dto.getStaffScore());
        entity.setStatus(dto.getStatus());
        entity.setCreateAt(dto.getCreateAt());
        entity.setUpdatedDate(dto.getUpdatedDate());

        // Map quan hệ
        if (dto.getSemester() != null) {
            SemesterEntity semesterEntity = new SemesterEntity();
            semesterEntity.setSemesterId(dto.getSemester().getSemesterId());
            entity.setSemesterEntity(semesterEntity);
        }

        if (dto.getStudent() != null) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setStudentId(dto.getStudent().getStudentId());
            entity.setStudentEntity(studentEntity);
        }

        if (dto.getConductFormDetailList() != null) {
            List<ConductFormDetailEntity> details = dto.getConductFormDetailList()
                    .stream()
                    .map(detailDTO -> {
                        ConductFormDetailEntity detailEntity = toDetailEntity(detailDTO);
                        detailEntity.setConductFormEntity(entity); // Gắn ngược lại để giữ mối quan hệ
                        return detailEntity;
                    })
                    .collect(Collectors.toList());
            entity.setConductFormDetailEntityList(details);
        }

        return entity;
    }
    // =========================
    //       DETAIL MAPPER
    // =========================
    public ConductFormDetailDTO toDetailDTO(ConductFormDetailEntity entity) {
        if (entity == null) return null;

        ConductFormDetailDTO dto = new ConductFormDetailDTO();
        dto.setConductFormDetailId(entity.getConductFormDetailId());
        dto.setSelfScore(entity.getSelfScore());
        dto.setClassMonitorScore(entity.getClassMonitorScore());
        dto.setStaffScore(entity.getStaff_score());
        dto.setComment(entity.getComment());
        dto.setFile(entity.getFile());
        dto.setPublicId(entity.getPublic_id());
        dto.setFile(entity.getFile());
        // Map CriteriaEntity
        if (entity.getCriteriaEntity() != null) {
            CriteriaDTO criteriaDTO = new CriteriaDTO();
            criteriaDTO.setCriteriaId(entity.getCriteriaEntity().getCriteriaId());
            criteriaDTO.setCriteriaName(entity.getCriteriaEntity().getCriteriaName());
            dto.setCriteria(criteriaDTO);
        }

        return dto;
    }

    public ConductFormDetailEntity toDetailEntity(ConductFormDetailDTO dto) {
        if (dto == null) return null;

        ConductFormDetailEntity entity = new ConductFormDetailEntity();
        entity.setConductFormDetailId(dto.getConductFormDetailId());
        entity.setSelfScore(dto.getSelfScore());
        entity.setClassMonitorScore(dto.getClassMonitorScore());
        entity.setStaff_score(dto.getStaffScore());
        entity.setComment(dto.getComment());
        entity.setFile(dto.getFile());
        entity.setPublic_id(dto.getPublicId());
        if (dto.getCriteria() != null) {
            CriteriaEntity criteriaEntity = new CriteriaEntity();
            criteriaEntity.setCriteriaId(dto.getCriteria().getCriteriaId());
            entity.setCriteriaEntity(criteriaEntity);
        }

        return entity;
    }
}

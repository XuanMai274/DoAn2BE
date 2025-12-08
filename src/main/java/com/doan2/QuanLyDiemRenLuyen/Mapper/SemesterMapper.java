package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.SemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.SemesterEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SemesterMapper {
    /**
     * Chuyển từ Entity sang DTO
     */
    public SemesterDTO toDTO(SemesterEntity entity) {
        if (entity == null) return null;

        SemesterDTO dto = new SemesterDTO();
        dto.setSemesterId(entity.getSemesterId());
        dto.setSemesterName(entity.getSemesterName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setYear(entity.getYear());
        dto.setOpen(entity.isOpen());
        dto.setEvaluationStartDate(entity.getEvaluationStartDate());
        dto.setEvaluationEndDate(entity.getEvaluationEndDate());
        return dto;
    }

    /**
     * Chuyển từ DTO sang Entity
     */
    public SemesterEntity toEntity(SemesterDTO dto) {
        if (dto == null) return null;

        SemesterEntity entity = new SemesterEntity();
        entity.setSemesterId(dto.getSemesterId());
        entity.setSemesterName(dto.getSemesterName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setYear(dto.getYear());

        // Thông thường, không set danh sách ConductForm ở đây
        // vì chúng ta chỉ lưu danh sách ID, cần Repository để fetch nếu cần
        // entity.setConductFormEntityList(...);

        return entity;
    }
}

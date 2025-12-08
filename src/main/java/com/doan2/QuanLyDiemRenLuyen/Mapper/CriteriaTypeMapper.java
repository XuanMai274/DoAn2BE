package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaTypeDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CriteriaTypeMapper {
    @Autowired
    @Lazy
    private CriteriaMapper criteriaMapper;
    // -------------------- Entity → DTO --------------------
    public CriteriaTypeDTO toDTO(CriteriaTypeEntity entity) {
        if (entity == null) return null;

        CriteriaTypeDTO dto = new CriteriaTypeDTO();
        dto.setCriteriaTypeId(entity.getCriteriaTypeId());
        dto.setCriteriaTypeName(entity.getCriteriaTypeName());
        dto.setMaxScore(entity.getMaxScore());
        dto.setIsActive(entity.isActive());
        // Ánh xạ danh sách tiêu chí con
        if (entity.getCriteriaEntityList() != null) {
            List<CriteriaDTO> criteriaDTOList = entity.getCriteriaEntityList()
                    .stream()
                    .map(criteriaMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setCriteriaEntityList(criteriaDTOList);
        }
        return dto;
    }

    // -------------------- DTO → Entity --------------------
    public CriteriaTypeEntity toEntity(CriteriaTypeDTO dto) {
        if (dto == null) return null;

        CriteriaTypeEntity entity = new CriteriaTypeEntity();
        entity.setCriteriaTypeId(dto.getCriteriaTypeId());
        entity.setCriteriaTypeName(dto.getCriteriaTypeName());
        entity.setMaxScore(dto.getMaxScore());
        entity.setActive(dto.getIsActive());
        // Ánh xạ danh sách tiêu chí con (nếu cần thiết khi thêm/sửa)
        if (dto.getCriteriaEntityList()!= null) {
            List<CriteriaEntity> criteriaEntities = dto.getCriteriaEntityList()
                    .stream()
                    .map(criteriaMapper::toEntity)
                    .peek(c -> c.setCriteriaTypeEntity(entity)) // gán ngược lại khóa ngoại
                    .collect(Collectors.toList());
            entity.setCriteriaEntityList(criteriaEntities);
        }
        return entity;
    }

}

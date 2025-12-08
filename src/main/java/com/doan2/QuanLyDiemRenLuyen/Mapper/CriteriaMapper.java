package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaTypeDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriteriaMapper {
    @Autowired
    private CriteriaTypeMapper criteriaTypeMapper;
    // -------------------- Entity → DTO --------------------
    public CriteriaDTO toDTO(CriteriaEntity entity) {
        if (entity == null) return null;

        CriteriaDTO dto = new CriteriaDTO();
        dto.setCriteriaId(entity.getCriteriaId());
        dto.setCriteriaName(entity.getCriteriaName());
        dto.setCriteriaDetail(entity.getCriteria_detail());
        dto.setMaxScore(entity.getMaxScore());

        // Chỉ map ID + name của loại tiêu chí để tránh vòng lặp
        if (entity.getCriteriaTypeEntity() != null) {
            CriteriaTypeDTO typeDTO = new CriteriaTypeDTO();
            typeDTO.setCriteriaTypeId(entity.getCriteriaTypeEntity().getCriteriaTypeId());
            typeDTO.setCriteriaTypeName(entity.getCriteriaTypeEntity().getCriteriaTypeName());
            dto.setCriteriaTypeEntity(typeDTO);
        }
        return dto;
    }

    // -------------------- DTO → Entity --------------------
    public CriteriaEntity toEntity(CriteriaDTO dto) {
        if (dto == null) return null;

        CriteriaEntity entity = new CriteriaEntity();
        entity.setCriteriaId(dto.getCriteriaId());
        entity.setCriteriaName(dto.getCriteriaName());
        entity.setCriteria_detail(dto.getCriteriaDetail());
        entity.setMaxScore(dto.getMaxScore());

        // Map ManyToOne: CriteriaType
        if (dto.getCriteriaTypeEntity() != null) {
            CriteriaTypeEntity typeEntity = new CriteriaTypeEntity();
            typeEntity.setCriteriaTypeId(dto.getCriteriaTypeEntity().getCriteriaTypeId());
            entity.setCriteriaTypeEntity(typeEntity);
        }

        return entity;
    }
}

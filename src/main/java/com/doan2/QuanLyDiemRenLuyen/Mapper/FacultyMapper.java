package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.ClassDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FacultyMapper {
    public  FacultyDTO toDTO(FacultyEntity entity) {
        if (entity == null) return null;

        FacultyDTO dto = new FacultyDTO();
        dto.setFacultyId(entity.getFacultyId());
        dto.setFacultyName(entity.getFacultyName());
        return dto;
    }

    public  FacultyEntity toEntity(FacultyDTO dto) {
        if (dto == null) return null;

        FacultyEntity entity = new FacultyEntity();
        entity.setFacultyId(dto.getFacultyId());
        entity.setFacultyName(dto.getFacultyName());
        return entity;
    }
}

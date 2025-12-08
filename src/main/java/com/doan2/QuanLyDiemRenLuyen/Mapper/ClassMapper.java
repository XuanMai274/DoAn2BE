package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.ClassDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ClassEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClassMapper {
    // =========================
    // Entity -> DTO
    // =========================
    public ClassDTO toDTO(ClassEntity entity) {
        if (entity == null) return null;

        ClassDTO dto = new ClassDTO();
        dto.setClassId(entity.getClassId());
        dto.setClassName(entity.getClassName());

        // Map faculty (chỉ lấy thông tin cần thiết để tránh vòng lặp)
        if (entity.getFacultyId() != null) {
            FacultyDTO facultyDTO = new FacultyDTO();
            facultyDTO.setFacultyId(entity.getFacultyId().getFacultyId());
            facultyDTO.setFacultyName(entity.getFacultyId().getFacultyName());
            dto.setFacultyEntity(facultyDTO);
        }
        return dto;
    }

    // =========================
    // DTO -> Entity
    // =========================
    public ClassEntity toEntity(ClassDTO dto) {
        if (dto == null) return null;

        ClassEntity entity = new ClassEntity();
        entity.setClassId(dto.getClassId());
        entity.setClassName(dto.getClassName());

        // Map faculty (chỉ set id, không load toàn bộ entity)
        if (dto.getFacultyEntity() != null) {
            FacultyEntity faculty = new FacultyEntity();
            faculty.setFacultyId(dto.getFacultyEntity().getFacultyId());
            entity.setFacultyId(faculty);
        }
        return entity;
    }
}

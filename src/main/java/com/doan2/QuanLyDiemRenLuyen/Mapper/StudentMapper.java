package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.AccountDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ClassDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public  StudentDTO toDTO(StudentEntity entity) {
        if (entity == null) return null;

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(entity.getStudentId());
        dto.setFullname(entity.getFullname());
        dto.setGender(entity.getGender());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setStatus(entity.getStatus());
        dto.setIsClassMonitor(entity.getIsClassMonitor());

        // --- Map ClassEntity → ClassDTO ---
        if (entity.getClassId() != null) {
            ClassDTO classDTO = new ClassDTO();
            classDTO.setClassId(entity.getClassId().getClassId());
            classDTO.setClassName(entity.getClassId().getClassName());
            dto.setClassDTO(classDTO);
        }

        // --- Map AccountEntity → AccountDTO ---
        if (entity.getAccountEntity() != null) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setAccountId(entity.getAccountEntity().getAccountId());
            accountDTO.setUsername(entity.getAccountEntity().getUsername());
            dto.setAccountDTO(accountDTO);
        }
        dto.setConductFormDTO(null);

        return dto;
    }
}

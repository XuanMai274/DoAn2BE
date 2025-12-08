package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.NotificationDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDTO toDTO(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(entity.getNotificationId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setCreateAt(entity.getCreateAt());
        dto.setUpdateAt(entity.getUpdateAt());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        // Quan hệ: Manager
        if (entity.getManagerEntity() != null) {
            ManagerDTO managerDTO = new ManagerDTO();
            managerDTO.setManagerId(entity.getManagerEntity().getManagerId());
            managerDTO.setFullname(entity.getManagerEntity().getFullname());
            managerDTO.setEmail(entity.getManagerEntity().getEmail());
            managerDTO.setPhoneNumber(entity.getManagerEntity().getPhoneNumber());
            // thêm các field khác nếu có
            dto.setManagerEntity(managerDTO);
        }

        return dto;
    }
    public NotificationEntity toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        NotificationEntity entity = new NotificationEntity();
        entity.setNotificationId(dto.getNotificationId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setCreateAt(dto.getCreateAt());
        entity.setUpdateAt(dto.getUpdateAt());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        // Quan hệ: Manager
        if (dto.getManagerEntity() != null) {
            ManagerEntity managerEntity = new ManagerEntity();
            managerEntity.setManagerId(dto.getManagerEntity().getManagerId());
            managerEntity.setFullname(dto.getManagerEntity().getFullname());
            managerEntity.setEmail(dto.getManagerEntity().getEmail());
            managerEntity.setPhoneNumber(dto.getManagerEntity().getPhoneNumber());
            // thêm các field khác nếu cần
            entity.setManagerEntity(managerEntity);
        }

        return entity;
    }
}

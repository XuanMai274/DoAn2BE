package com.doan2.QuanLyDiemRenLuyen.Mapper;
import com.doan2.QuanLyDiemRenLuyen.DTO.*;
import com.doan2.QuanLyDiemRenLuyen.Entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ManagerMapper {

    // Entity → DTO
    public ManagerDTO toDTO(ManagerEntity entity) {
        if (entity == null) return null;

        ManagerDTO dto = new ManagerDTO();
        dto.setManagerId(entity.getManagerId());
        dto.setFullname(entity.getFullname());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setStatus(entity.getStatus());

        // Faculty
        if (entity.getFacultyEntity() != null) {
            FacultyDTO facultyDTO = new FacultyDTO();
            facultyDTO.setFacultyId(entity.getFacultyEntity().getFacultyId());
            facultyDTO.setFacultyName(entity.getFacultyEntity().getFacultyName());
            dto.setFacultyEntity(facultyDTO);
        }

        // Account
        if (entity.getAccountEntity() != null) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setAccountId(entity.getAccountEntity().getAccountId());
            accountDTO.setUsername(entity.getAccountEntity().getUsername());
            accountDTO.setPassword(entity.getAccountEntity().getPassword());
            accountDTO.setEnable(entity.getAccountEntity().getEnable());
            dto.setAccountEntity(accountDTO);
        }

        // Notification list
        if (entity.getNotificationEntity() != null) {
            dto.setNotificationEntity(
                    entity.getNotificationEntity().stream().map(notification -> {
                        NotificationDTO n = new NotificationDTO();
                        n.setNotificationId(notification.getNotificationId());
                        n.setTitle(notification.getTitle());
                        n.setContent(notification.getContent());
                        return n;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }

    // DTO → Entity
    public ManagerEntity toEntity(ManagerDTO dto) {
        if (dto == null) return null;

        ManagerEntity entity = new ManagerEntity();
        entity.setManagerId(dto.getManagerId());
        entity.setFullname(dto.getFullname());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setStatus(dto.getStatus());

        // Faculty
        if (dto.getFacultyEntity() != null) {
            FacultyEntity faculty = new FacultyEntity();
            faculty.setFacultyId(dto.getFacultyEntity().getFacultyId());
            faculty.setFacultyName(dto.getFacultyEntity().getFacultyName());
            entity.setFacultyEntity(faculty);
        }

        // Account
        if (dto.getAccountEntity() != null) {
            AccountEntity account = new AccountEntity();
            account.setAccountId(dto.getAccountEntity().getAccountId());
            account.setUsername(dto.getAccountEntity().getUsername());
            account.setPassword(dto.getAccountEntity().getPassword());
            account.setEnable(dto.getAccountEntity().getEnable());
            entity.setAccountEntity(account);
        }

        // Notification list
        if (dto.getNotificationEntity() != null) {
            entity.setNotificationEntity(
                    dto.getNotificationEntity().stream().map(n -> {
                        NotificationEntity notification = new NotificationEntity();
                        notification.setNotificationId(n.getNotificationId());
                        notification.setTitle(n.getTitle());
                        notification.setContent(n.getContent());
                        return notification;
                    }).collect(Collectors.toList())
            );
        }

        return entity;
    }
}

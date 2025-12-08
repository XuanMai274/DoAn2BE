package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.NotificationDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.NotificationMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.FacultyRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.ManagerRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.NotificationReadRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.NotificationRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImplement implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    NotificationReadRepository notificationReadRepository;
    @Override
    public NotificationDTO addNotification(NotificationDTO dto) {
        try {
            NotificationEntity entity;
            // 1. Cập nhật thông báo (ID != 0)
            if (dto.getNotificationId() != 0) {
                entity = notificationRepository.findByNotificationId(dto.getNotificationId());

                if (entity == null) {
                    throw new RuntimeException("Notification not found with ID = " + dto.getNotificationId());
                }

                // Cập nhật thủ công từng trường (để không ghi đè quan hệ bị null từ DTO)
                entity.setTitle(dto.getTitle());
                entity.setContent(dto.getContent());
                entity.setType(dto.getType());
                entity.setStatus(dto.getStatus());
                entity.setStartDate(dto.getStartDate());
                entity.setEndDate(dto.getEndDate());

                // Cập nhật Manager nếu DTO có
                if (dto.getManagerEntity() != null) {
                    entity.setManagerEntity(managerRepository.findById(
                            dto.getManagerEntity().getManagerId()
                    ).orElse(null));
                }

                // Cập nhật Faculty nếu DTO có
                if (dto.getFacultyDTO() != null) {
                    entity.setFacultyEntity(facultyRepository.findById(
                            dto.getFacultyDTO().getFacultyId()
                    ).orElse(null));
                }

                notificationRepository.save(entity);
                return notificationMapper.toDTO(entity);
            }
            // 2. Thêm mới thông báo (ID == 0)
            entity = new NotificationEntity();
            entity.setTitle(dto.getTitle());
            entity.setContent(dto.getContent());
            entity.setStatus(dto.getStatus());
            entity.setType(dto.getType());
            entity.setStartDate(dto.getStartDate());
            entity.setEndDate(dto.getEndDate());

            // Set manager
            if (dto.getManagerEntity() != null) {
                entity.setManagerEntity(managerRepository.findById(
                        dto.getManagerEntity().getManagerId()
                ).orElse(null));
            }

            // Set faculty
            if (dto.getFacultyDTO() != null) {
                entity.setFacultyEntity(facultyRepository.findById(
                        dto.getFacultyDTO().getFacultyId()
                ).orElse(null));
            }

            notificationRepository.save(entity);

            return notificationMapper.toDTO(entity);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm/cập nhật thông báo: " + e.getMessage(), e);
        }
    }


    @Override
    public List<NotificationDTO> getAllNotificationByManagerId(int id,int faculty) {
        List<NotificationEntity> notificationEntityList=notificationRepository.findAllByManagerId(id,faculty);
        if(notificationEntityList!=null){
            List<NotificationDTO> notificationDTOS=new ArrayList<>();
            for(NotificationEntity n: notificationEntityList){
                notificationDTOS.add(notificationMapper.toDTO(n));
            }
            return notificationDTOS;
        }
        return List.of();
    }

    @Override
    public List<NotificationDTO> getAllNotificationsForStudent(int studentId,int facultyId) {
        List<Object[]> list = notificationRepository.findAllWithReadStatusByFaculty(studentId,facultyId);
        List<NotificationDTO> result = new ArrayList<>();
        for (Object[] obj : list) {
            NotificationEntity entity = (NotificationEntity) obj[0];
            boolean read = (Boolean) obj[1];
            NotificationDTO dto = notificationMapper.toDTO(entity);
            dto.setRead(read);
            result.add(dto);
        }
        return result;
    }
    @Override
    @Transactional
    public boolean deleteNotification(int id) {
        if (!notificationRepository.existsById(id)) return false;

        // Xóa bảng notification_read trước (tránh FK constraint)
        notificationReadRepository.deleteByNotification_notificationId(id);

        // Xóa thông báo
        notificationRepository.deleteById(id);

        return true;
    }
}

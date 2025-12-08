package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.NotificationDTO;

import java.util.List;

public interface NotificationService {
    NotificationDTO addNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getAllNotificationByManagerId (int id,int facultyId);
    List<NotificationDTO> getAllNotificationsForStudent(int studentId,int facultyId);
    boolean deleteNotification(int id);
}

package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationReadEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import com.doan2.QuanLyDiemRenLuyen.Repository.NotificationReadRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.NotificationRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.StudentRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.NotificationReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationReadServiceImplement implements NotificationReadService {
    @Autowired
    NotificationReadRepository notificationReadRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    StudentRepository studentRepository;
    public NotificationReadServiceImplement(NotificationReadRepository notificationReadRepository) {
        this.notificationReadRepository = notificationReadRepository;
    }

    /** Đánh dấu thông báo là đã đọc */
    @Override
    public void markAsRead(int notificationId, int studentId) {
        // lấy lên thông báo
        NotificationEntity notification= notificationRepository.findByNotificationId(notificationId);
        StudentEntity student=studentRepository.findByStudentId(studentId);
        boolean exists = notificationReadRepository.findByNotificationAndStudent(notification, student).isPresent();
        if (!exists) {
            NotificationReadEntity read = new NotificationReadEntity();
            read.setNotification(notification);
            read.setStudent(student);
            read.setReadAt(LocalDateTime.now());
            notificationReadRepository.save(read);
        }
    }

    /** Kiểm tra sinh viên đã đọc hay chưa */
    @Override
    public boolean isRead(int notificationId, int studentId) {
        NotificationEntity notification= notificationRepository.findByNotificationId(notificationId);
        StudentEntity student=studentRepository.findByStudentId(studentId);
        return notificationReadRepository.findByNotificationAndStudent(notification, student).isPresent();
    }
}

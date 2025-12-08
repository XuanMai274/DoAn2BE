package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationReadEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationReadRepository extends JpaRepository<NotificationReadEntity, Integer> {
    Optional<NotificationReadEntity> findByNotificationAndStudent(NotificationEntity notification, StudentEntity student);
    void deleteByNotification_notificationId(int notificationId);
}

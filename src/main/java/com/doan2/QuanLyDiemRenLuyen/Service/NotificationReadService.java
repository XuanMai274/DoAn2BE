package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.Entity.NotificationEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;

public interface NotificationReadService {
    void markAsRead(int notification, int student);
    boolean isRead(int notification, int student);
}

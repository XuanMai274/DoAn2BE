package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    private int notificationId;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String type;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    // Quan hệ
    private ManagerDTO managerEntity;
    private boolean read;
    private FacultyDTO facultyDTO;

}

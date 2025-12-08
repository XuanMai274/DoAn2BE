package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ManagerDTO {
    private int managerId;
    private String fullname;
    private String email;
    private String phoneNumber;
    private String status;

    private FacultyDTO facultyEntity;
    private AccountDTO accountEntity;
    private List<NotificationDTO> notificationEntity;
}

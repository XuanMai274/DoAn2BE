package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private int accountId;
    private String username;
    private String password;
    private Boolean enable;

    private RoleDTO roleEntity;
    private StudentDTO studentEntity;
    private ManagerDTO managerEntity;
}

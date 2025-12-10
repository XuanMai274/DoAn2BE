package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest  {
    private int accountId;
    private String currentPassword;
    private String newPassword;
}

package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}

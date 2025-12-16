package com.doan2.QuanLyDiemRenLuyen.Service;

public interface PasswordResetService {
    public boolean generateResetToken(String email);
    public void resetPassword(String token, String newPassword);
}

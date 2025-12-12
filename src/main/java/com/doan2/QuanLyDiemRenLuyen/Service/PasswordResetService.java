package com.doan2.QuanLyDiemRenLuyen.Service;

public interface PasswordResetService {
    public void generateResetToken(String email);
    public void resetPassword(String token, String newPassword);
}

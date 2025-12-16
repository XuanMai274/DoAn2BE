package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.ResetPasswordRequest;
import com.doan2.QuanLyDiemRenLuyen.Service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authenticate")
public class ForgotPasswordAPI {
    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        boolean success = passwordResetService.generateResetToken(email);

        if (!success) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Email không tồn tại"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Vui lòng kiểm tra email để đặt lại mật khẩu!")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công!"));
    }
}

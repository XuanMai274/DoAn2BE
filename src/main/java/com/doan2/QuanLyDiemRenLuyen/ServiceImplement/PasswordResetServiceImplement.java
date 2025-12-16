package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.Entity.AccountEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.PasswordResetToken;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import com.doan2.QuanLyDiemRenLuyen.Repository.AccountRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.PasswordResetTokenRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.StudentRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.EmailService;
import com.doan2.QuanLyDiemRenLuyen.Service.PasswordResetService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class PasswordResetServiceImplement implements PasswordResetService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public boolean generateResetToken(String email) {
        // Tìm người dùng qua email student
        StudentEntity student = studentRepository.findByEmail(email);
        if (student == null) {
            return false;
        }
        AccountEntity account = student.getAccountEntity();

        // Xoá token cũ (1 tài khoản chỉ có 1 token)
        tokenRepository.deleteByAccount(account);

        // Tạo token mới
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setAccount(account);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        // Gửi email
        String link = "http://localhost:4200/reset-password/" + token;

        String subject = "Yêu cầu đặt lại mật khẩu – Hệ thống Quản lý Điểm Rèn Luyện";

        String content =
                "<div style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>"
                        + "<h2 style='color: #2c3e50;'>Đặt lại mật khẩu</h2>"
                        + "<p>Chào bạn <strong>" + student.getFullname() + "</strong>,</p>"
                        + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn trên "
                        + " <strong>Hệ thống Quản lý Điểm Rèn Luyện</strong>.</p>"
                        + "<p>Vui lòng nhấn vào nút bên dưới để tiến hành đổi mật khẩu:</p>"

                        + "<p style='margin: 25px 0;'>"
                        + "<a href='" + link + "' "
                        + "style='background-color: #1e88e5; color: white; padding: 12px 20px; "
                        + "text-decoration: none; border-radius: 5px; font-weight: bold;'>"
                        + "Đặt lại mật khẩu</a></p>"

                        + "<p>Nếu nút không hoạt động, hãy sao chép liên kết sau và dán vào trình duyệt:</p>"
                        + "<p><a href='" + link + "'>" + link + "</a></p>"

                        + "<p><em>Lưu ý: Liên kết này có hiệu lực trong 15 phút.</em></p>"

                        + "<hr style='margin-top: 30px;'/>"
                        + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này "
                        + "hoặc liên hệ quản trị viên để được hỗ trợ.</p>"
                        + "<p>Trân trọng,<br/>"
                        + "<strong>Ban Quản Trị Hệ Thống</strong></p>"
                        + "</div>";

        emailService.sendEmail(email, subject, content);
        return true;
    }
    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if(resetToken==null){
            throw new RuntimeException("Token đã hết hạn");
        }
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token đã hết hạn");

        AccountEntity account = resetToken.getAccount();
        // bcrypt
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        // Xóa token
        tokenRepository.delete(resetToken);
    }


}

package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplement implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // <--- quan trọng: true = HTML mode

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Không gửi được email");
        }
    }
}

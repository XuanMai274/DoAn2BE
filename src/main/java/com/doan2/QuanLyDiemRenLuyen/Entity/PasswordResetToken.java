package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(nullable = false)
    private LocalDateTime expiryDate;
}

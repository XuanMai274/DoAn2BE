package com.doan2.QuanLyDiemRenLuyen.Repository;

import com.doan2.QuanLyDiemRenLuyen.Entity.AccountEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    void deleteByAccount(AccountEntity account);
}

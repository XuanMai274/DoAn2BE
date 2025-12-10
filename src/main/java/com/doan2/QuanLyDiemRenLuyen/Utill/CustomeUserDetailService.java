package com.doan2.QuanLyDiemRenLuyen.Utill;

import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.AccountEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import com.doan2.QuanLyDiemRenLuyen.Repository.AccountRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomeUserDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomeUserDetailService.class);
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StudentService studentService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findByUsername(username);

        if (accountEntity == null) {
            logger.warn("User not found: {}", username);
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }

        // ========== KIỂM TRA TRẠNG THÁI ENABLE ==========
        if (Boolean.FALSE.equals(accountEntity.getEnable())) {
            logger.warn("User disabled: {}", username);
            throw new UsernameNotFoundException("Tài khoản đã bị khóa");
        }

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        // ========== PHÂN QUYỀN ==========
        if (accountEntity.getRole() != null) {

            // ROLE STUDENT
            if (accountEntity.getRole().getRoleName().equals("STUDENT")) {

                grantedAuthorityList.add(new SimpleGrantedAuthority("STUDENT"));

                StudentEntity student = accountEntity.getStudentEntity();
                if (student != null && student.getIsClassMonitor()) {
                    grantedAuthorityList.add(new SimpleGrantedAuthority("CLASS_MONITOR"));
                }

            }
            // ROLES khác (MANAGER, ADMIN, ...)
            else {
                grantedAuthorityList.add(new SimpleGrantedAuthority(accountEntity.getRole().getRoleName()));
            }
        }

        return new CustomeUserDetails(accountEntity, grantedAuthorityList);
    }
}

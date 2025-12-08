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
        // goi repo kiểm tra xem username đó có tồn tại trong hệ thong hay không
        AccountEntity accountEntity = accountRepository.findByUsername(username);
        // kiểm tra xem nếu người dùng không tồn tại trong hệ thống
        List<GrantedAuthority> grantedAuthorityList;
        if (accountEntity == null) {
            logger.warn("User not found or inactive: {}", username);
            //return new CustomeUserDetails(null,null);
            throw new UsernameNotFoundException("User not found");
        } else {
            grantedAuthorityList = new ArrayList<>();
            if (accountEntity.getRole() != null) {
                if(accountEntity.getRole().getRoleName().equals("STUDENT")){
                    grantedAuthorityList.add(new SimpleGrantedAuthority(accountEntity.getRole().getRoleName()));
                    StudentEntity student = accountEntity.getStudentEntity();
                    if(student.getIsClassMonitor()){
                        grantedAuthorityList.add(new SimpleGrantedAuthority("CLASS_MONITOR"));
                    }
                }
                else{
                    grantedAuthorityList.add(new SimpleGrantedAuthority(accountEntity.getRole().getRoleName()));
                }

            }
        }
        return new CustomeUserDetails(accountEntity, grantedAuthorityList);
    }
}

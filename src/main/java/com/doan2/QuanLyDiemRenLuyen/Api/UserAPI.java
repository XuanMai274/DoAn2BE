package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.ManagerService;
import com.doan2.QuanLyDiemRenLuyen.Service.StudentService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAPI {
    @Autowired
    StudentService studentService;
    @Autowired
    ManagerService managerService;
    @GetMapping("/user")
    ResponseEntity<Object> getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomeUserDetails userDetails=(CustomeUserDetails) authentication.getPrincipal();
        if (!(authentication.getPrincipal() instanceof CustomeUserDetails)) {
            return ResponseEntity.status(401).body("Chưa đăng nhập hoặc token không hợp lệ");
        }
        String role=userDetails.getAccountEntity().getRole().getRoleName();
        if(role.equals("STUDENT")){
            StudentDTO studentDTO=studentService.findByAccountId(userDetails.getAccountEntity().getAccountId());
            return ResponseEntity.ok(studentDTO);
        }
        else if(role.equals("MANAGER")){
            ManagerDTO managerDTO=managerService.findByAccountId(userDetails.getAccountEntity().getAccountId());
            return ResponseEntity.ok(managerDTO);
        }
        return null;
    }
}

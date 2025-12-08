package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.ClassDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.ClassService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ClassAPI {
    @Autowired
    ClassService classService;
    @GetMapping("/manager/class/getAll")
    public ResponseEntity<List<ClassDTO>> findAll(){
        // lấy thông tin khoa
        // kiểm tra đăng nhập và lấy id của người dùng hiện tại
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((List<ClassDTO>) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        List<ClassDTO> classDTOS=classService.findAll(faculty);
        if(classDTOS!=null){
            return ResponseEntity.ok(classDTOS);
        }
        return null;
    }
    @GetMapping("/manager/class/faculty/{facultyId}")
    public ResponseEntity<List<ClassDTO>> findByFaculty(@PathVariable("facultyId") int facultyId){
        List<ClassDTO> classDTOS= classService.findByFacultyId(facultyId);
        if(classDTOS!=null){
            return ResponseEntity.ok(classDTOS);
        }
        return null;
    }
}

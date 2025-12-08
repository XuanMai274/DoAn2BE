package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.ConductFormDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.ConductFormService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/conductForm")
public class ConductFormAPI1 {
    @Autowired
    ConductFormService conductFormService;
   // @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> addConductForm(@RequestBody ConductFormDTO conductFormDTO){
        // lấy thông tin của người dùng hiện tại
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        if(authentication!=null){
            UserDetails user=(UserDetails) authentication.getPrincipal();
            // ép user về customUserDetail
            CustomeUserDetails custome=(CustomeUserDetails) user;
            int id=custome.getAccountEntity().getStudentEntity().getStudentId();
            System.out.println("Mã sinh viên hiện tại là: "+id);
            StudentDTO studentDTO=new StudentDTO();
            studentDTO.setStudentId(id);
            conductFormDTO.setStudent(studentDTO);
            // kiểm tra xem người dùng có upload hình ảnh hay khong
            ConductFormDTO conductFormDTONew=conductFormService.addConductForm(conductFormDTO);
            if(conductFormDTONew!=null){
                response.put("success", true);
                response.put("conductForm",conductFormDTONew);
                return ResponseEntity.ok(response);
            }
            else {
                response.put("success", false);
                response.put("conductForm", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
        else{
            response.put("success", false);
            response.put("conductForm", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

}

package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.*;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import com.doan2.QuanLyDiemRenLuyen.Service.ConductFormService;
import com.doan2.QuanLyDiemRenLuyen.Service.ManagerService;
import com.doan2.QuanLyDiemRenLuyen.Service.StudentService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ManagerAPI {
    @Autowired
    StudentService studentService;
    @Autowired
    ConductFormService conductFormService;
    @Autowired
    ManagerService managerService;
    @GetMapping("manager/statistical/{semesterId}/{classId}")
    public Map<String, List<StudentDTO>> findStudentsSubmittedConductForm(@PathVariable("semesterId") int semesterId, @PathVariable("classId") int classId){
        Map<String,List<StudentDTO>> response=new HashMap<>();
        List<StudentDTO> submitted=studentService.findStudentsSubmittedConductForm(semesterId,classId);
        List<StudentDTO> notSubmitted=studentService.findStudentsNotSubmittedConductForm(semesterId,classId);
        response.put("submitted",submitted);
        response.put("notSubmitted",notSubmitted);
        return response;
    }

    @GetMapping("manager/statistical/avgClass")
    public List<ClassAverageScoreDTO> getAverageScoreByFaculty(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return (List<ClassAverageScoreDTO>) ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((List<ClassAverageScoreDTO>) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        return conductFormService.getAverageScoreByFaculty(faculty);
    }



}

package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.FeedbackDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.CloudinaryService;
import com.doan2.QuanLyDiemRenLuyen.Service.FeedbackService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FeedbackAPI {
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    FeedbackService feedbackService;
    @PostMapping(
            value = "student/feedback/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FeedbackDTO> create(
            @RequestPart("feedbackDTO") String feedbackJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            FeedbackDTO feedbackDTO = mapper.readValue(feedbackJson, FeedbackDTO.class);

            // Upload file nếu có
            if (file != null && !file.isEmpty()) {
                Map<String, String> uploadResult = cloudinaryService.uploadImage(file);
                feedbackDTO.setImage(uploadResult.get("url"));
            }

            // Lấy user từ Security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
            int studentId = user.getAccountEntity().getStudentEntity().getStudentId();
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setStudentId(studentId);
            feedbackDTO.setStudentDTO(studentDTO);

            FeedbackDTO saved = feedbackService.create(feedbackDTO);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("student/feedback/findAll")
    public ResponseEntity<List<FeedbackDTO>> findAllByStudent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
        int studentId = user.getAccountEntity().getStudentEntity().getStudentId();
        List<FeedbackDTO> feedbackDTOS=feedbackService.findAllByStudent(studentId);
        if(feedbackDTOS!=null){
            return ResponseEntity.ok(feedbackDTOS);
        }
        return null;
    }
    @GetMapping("manager/feedback/findAll")
    public ResponseEntity<List<FeedbackDTO>> findAllForManager(){
        // lấy thông tin managerId hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
        int managerId = user.getAccountEntity().getManagerEntity().getManagerId();
        List<FeedbackDTO> feedbackDTOS=feedbackService.findAll(managerId);
        if(feedbackDTOS!=null){
            return ResponseEntity.ok(feedbackDTOS);
        }
        return null;
    }
    @PostMapping("manager/feedback/update")
    public ResponseEntity<FeedbackDTO> Manager_update(@RequestBody FeedbackDTO feedbackDTO){
        // lấy thông tin của người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
        int managerId = user.getAccountEntity().getManagerEntity().getManagerId();
        ManagerDTO managerDTO=new ManagerDTO();
        managerDTO.setManagerId(managerId);
        feedbackDTO.setManagerDTO(managerDTO);
        FeedbackDTO feedbackDTO1= feedbackService.update(feedbackDTO);
        if(feedbackDTO1!=null){
            return ResponseEntity.ok(feedbackDTO1);
        }
        return null;

    }

}

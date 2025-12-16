package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.FacultySemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.SemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.FacultySemesterService;
import com.doan2.QuanLyDiemRenLuyen.Service.SemesterService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/manager/semester")
public class SemesterAPI {
    @Autowired
    SemesterService semesterService;
    @Autowired
    FacultySemesterService facultySemesterService;
    @PostMapping("/manager1/semester/add")
    public ResponseEntity<Map<String,Object>> addSemester(@RequestBody SemesterDTO semesterDTO){
        // thêm vào csdl
        SemesterDTO semesterDTONew=semesterService.addSemester(semesterDTO);
        Map<String, Object> response = new HashMap<>();
        if(semesterDTONew!=null){
            response.put("success", true);
            response.put("semester",semesterDTONew);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("success", false);
            response.put("semester", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/semester/getAll")
    public ResponseEntity<List<SemesterDTO>> getAllSemesters() {
        List<SemesterDTO> semesters = semesterService.findAll();
        return ResponseEntity.ok(semesters);
    }
    // hàm sửa học kì
    @PostMapping("/manager1/semester/update")
    public ResponseEntity<SemesterDTO> update(@RequestBody SemesterDTO semesterDTO){
        try{
            SemesterDTO semesterDTO1=semesterService.addSemester(semesterDTO);
            return ResponseEntity.ok(semesterDTO1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping("/semester/Open")
    public ResponseEntity<List<FacultySemesterDTO>> findByIsOpenTrue(){
        // lấy id theo khoa hiện tại
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonList((FacultySemesterDTO) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa")));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=0;
        if(customeUserDetails.getAccountEntity().getRole().getRoleName().equals("STUDENT")){
            // lấy theo student
            faculty=customeUserDetails.getAccountEntity().getStudentEntity().getClassId().getFacultyId().getFacultyId();
        }else if(customeUserDetails.getAccountEntity().getRole().getRoleName().equals("MANAGER")){
            faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        }
        List<FacultySemesterDTO> semesterDTOS=facultySemesterService.findByIsOpenTrue(faculty);
        return ResponseEntity.ok(semesterDTOS);
    }
    @GetMapping("/student/semester/Open")
    public ResponseEntity<List<FacultySemesterDTO>> findByIsOpenTrueForStudent(){
        // lấy id theo khoa hiện tại
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonList((FacultySemesterDTO) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa")));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int studentId=customeUserDetails.getAccountEntity().getStudentEntity().getStudentId();
        int faculty=customeUserDetails.getAccountEntity().getStudentEntity().getClassId().getFacultyId().getFacultyId();
        List<FacultySemesterDTO> semesterDTOS=facultySemesterService.findByIsOpenForStudent(faculty,studentId);
        return ResponseEntity.ok(semesterDTOS);
    }
    // Mở học kì đánh giá rèn luyeen
    @PostMapping("manager/createBatch")
    public ResponseEntity<FacultySemesterDTO> createBatch(@RequestBody FacultySemesterDTO dto) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((FacultySemesterDTO) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        FacultyDTO facultyDTO=new FacultyDTO();
        facultyDTO.setFacultyId(faculty);
        dto.setFacultyDTO(facultyDTO);
        FacultySemesterDTO result = facultySemesterService.createBatch(dto);
        return ResponseEntity.ok(result);
    }
    // cập nhật học kì đánh giá rèn luyện
    @PostMapping("manager/updateBatch")
    public ResponseEntity<FacultySemesterDTO> updateBatch(@RequestBody FacultySemesterDTO dto) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((FacultySemesterDTO) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        FacultyDTO facultyDTO=new FacultyDTO();
        facultyDTO.setFacultyId(faculty);
        dto.setFacultyDTO(facultyDTO);
        FacultySemesterDTO result = facultySemesterService.updateBatch(dto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/manager/semester/opened")
    public ResponseEntity<List<FacultySemesterDTO>> findSemesterOpened(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((List<FacultySemesterDTO>) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        List<FacultySemesterDTO> semesterDTOList=facultySemesterService.findSemesterOpened(faculty);
        return ResponseEntity.ok(semesterDTOList);
    }
    // tức là học kì chưa từng mở
    @GetMapping("/manager/semester/available")
    public ResponseEntity<List<SemesterDTO>> availableSemesters(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((List<SemesterDTO>) Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int faculty=customeUserDetails.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        List<SemesterDTO> semesterDTOList=facultySemesterService.availableSemesters(faculty);
        return ResponseEntity.ok(semesterDTOList);
    }

}

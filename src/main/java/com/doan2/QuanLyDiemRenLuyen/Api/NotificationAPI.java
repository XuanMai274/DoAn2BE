package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.NotificationDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.NotificationReadService;
import com.doan2.QuanLyDiemRenLuyen.Service.NotificationService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class NotificationAPI {
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationReadService notificationReadService;
    // lấy lên tất cả các thông báo mà người quản lý hiện tại đã tạo
    @GetMapping("/manager/notification/getAll")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationByManagerId (){
        //Kiểm tra đăng nhập và lấy id của người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
        int managerId= user.getAccountEntity().getManagerEntity().getManagerId();
        int facultyId= user.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
        List<NotificationDTO> notificationDTOS=notificationService.getAllNotificationByManagerId(managerId,facultyId);
        if(notificationDTOS!=null){
            return ResponseEntity.ok(notificationDTOS);
        }
        return null;
    }
    // lay tất cả thông báo cho sinh viên
    @GetMapping("/notification/getAll")
    public  ResponseEntity<List<NotificationDTO>> getAll(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return null;
        }
        int studentId= ((CustomeUserDetails) userDetails).getAccountEntity().getStudentEntity().getStudentId();
        // lấy thêm thông tin khoa
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int facultyId= customeUserDetails.getAccountEntity().getStudentEntity().getClassId().getFacultyId().getFacultyId();
        List<NotificationDTO> notificationDTOS=notificationService.getAllNotificationsForStudent(studentId,facultyId);
        if(notificationDTOS!=null){
            return ResponseEntity.ok(notificationDTOS);
        }
        return null;

    }
    @PostMapping("/manager/notification/add")
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO){
        try {
            //Kiểm tra đăng nhập và lấy id của người dùng hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
                return ResponseEntity.badRequest().body((NotificationDTO) Collections.emptyList());
            }
            CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
            int managerId= user.getAccountEntity().getManagerEntity().getManagerId();
            int facultyId= user.getAccountEntity().getManagerEntity().getFacultyEntity().getFacultyId();
            ManagerDTO managerDTO=new ManagerDTO();
            managerDTO.setManagerId(managerId);
            notificationDTO.setManagerEntity(managerDTO);
            FacultyDTO facultyDTO=new FacultyDTO();
            facultyDTO.setFacultyId(facultyId);
            notificationDTO.setFacultyDTO(facultyDTO);
            NotificationDTO notificationDTO1=notificationService.addNotification(notificationDTO);
            if(notificationDTO1!=null){
                return ResponseEntity.ok(notificationDTO1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @PostMapping("/manager/notification/update")
    public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO){
        try {
            NotificationDTO notificationDTO1=notificationService.addNotification(notificationDTO);
            if(notificationDTO1!=null){
                return ResponseEntity.ok(notificationDTO1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /** Đánh dấu là đã đọc */
    @PostMapping("student/notification/read/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable int notificationId) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return null;
        }
        int studentId= ((CustomeUserDetails) userDetails).getAccountEntity().getStudentEntity().getStudentId();
        notificationReadService.markAsRead(notificationId, studentId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/manager/notification/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable int notificationId) {
        boolean deleted = notificationService.deleteNotification(notificationId);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy thông báo với ID: " + notificationId);
        }

        return ResponseEntity.ok("Xóa thông báo thành công");
    }
}

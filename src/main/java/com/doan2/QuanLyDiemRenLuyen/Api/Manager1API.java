package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.ConductFormService;
import com.doan2.QuanLyDiemRenLuyen.Service.ManagerService;
import com.doan2.QuanLyDiemRenLuyen.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class Manager1API {
    @Autowired
    StudentService studentService;
    @Autowired
    ConductFormService conductFormService;
    @Autowired
    ManagerService managerService;
    // lấy danh sách quản lý
    @GetMapping("/manager1/manager/getAll")
    public ResponseEntity<List<ManagerDTO>> findAllManager(){
        List<ManagerDTO> managerDTOS=managerService.findAllManager();
        if(managerDTOS!=null){
            return ResponseEntity.ok(managerDTOS);
        }
        return null;
    }
    @PostMapping("/manager1/manager/add")
    public ManagerDTO addManager(@RequestBody ManagerDTO dto) {
        return managerService.createManager(dto);
    }

    // Cập nhật quản lý
    @PutMapping("/manager1/manager/update")
    public ManagerDTO updateManager(@RequestBody ManagerDTO dto) {
        return managerService.updateManager(dto);
    }
}

package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FacultyAPI {
    @Autowired
    FacultyService facultyService;
    @GetMapping("/manager/faculty/findAll")
    public ResponseEntity<List<FacultyDTO>> findAll(){
        List<FacultyDTO> facultyDTOS=facultyService.findAll();
        return ResponseEntity.ok(facultyDTOS);
    }
}

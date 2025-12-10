package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaTypeDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.CriteriaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/manager/criteriaType")
public class CriteriaTypeAPI {
    @Autowired
    CriteriaTypeService criteriaTypeService;
    @PostMapping("/manager1/criteriaType/create")
    public ResponseEntity<Map<String,Object>> CriteriaTypeAdd(@RequestBody CriteriaTypeDTO criteriaTypeDTO){
        // thêm vào cơ sở dữ liệu
        CriteriaTypeDTO criteriaTypeDTONew=criteriaTypeService.addCriteriaType(criteriaTypeDTO);
        Map<String, Object> response = new HashMap<>();
        if(criteriaTypeDTONew!=null){
            response.put("success", true);
            response.put("criteriaType",criteriaTypeDTONew);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("success", false);
            response.put("criteriaType", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    public ResponseEntity<Map<String,String>> CriteriaTypeUpdate(@RequestBody CriteriaTypeDTO criteriaTypeDTO){
        return null;
    }
    //sinh viên
    @GetMapping("/criteriaType/getAll")
    public ResponseEntity<List<CriteriaTypeDTO>> getAllCriteriaTypeFor() {
        List<CriteriaTypeDTO> criteriaTypeDTO = criteriaTypeService.findAll();
        return ResponseEntity.ok(criteriaTypeDTO);
    }
    //admin
    @GetMapping("/manager1/criteriaType/getAll")
    public ResponseEntity<List<CriteriaTypeDTO>> getAllCriteriaTypeForManager() {
        List<CriteriaTypeDTO> semesters = criteriaTypeService.findAllForManager();
        return ResponseEntity.ok(semesters);
    }
    //sinh viên
    @GetMapping("/student/criteriaType/getAll/{conductFormId}")
    public ResponseEntity<List<CriteriaTypeDTO>> getAllCriteriaTypeForStudent(@PathVariable("conductFormId") int conductFormId) {
        List<CriteriaTypeDTO> semesters = criteriaTypeService.findAllByConductFormId(conductFormId);
        return ResponseEntity.ok(semesters);
    }
    @PostMapping("/manager1/criteriaType/update")
    public ResponseEntity<CriteriaTypeDTO> update(@RequestBody CriteriaTypeDTO criteriaTypeDTO){
        CriteriaTypeDTO criteriaTypeDTO1=criteriaTypeService.addCriteriaType(criteriaTypeDTO);
        return ResponseEntity.ok(criteriaTypeDTO1);
    }
}

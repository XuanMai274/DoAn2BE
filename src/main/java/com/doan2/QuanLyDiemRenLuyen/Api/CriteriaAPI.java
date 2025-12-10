package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaTypeDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.CriteriaService;
import com.doan2.QuanLyDiemRenLuyen.Service.CriteriaTypeService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/manager/criteria")
public class CriteriaAPI {
    @Autowired
    CriteriaService criteriaService;
    @PostMapping("/manager1/criteria/create")
    public ResponseEntity<Map<String,Object>> CriteriaTypeAdd(@RequestBody CriteriaDTO criteriaDTO){
        // thêm vào cơ sở dữ liệu
        CriteriaDTO criteriaDTONew=criteriaService.addCriteria(criteriaDTO);
        Map<String, Object> response = new HashMap<>();
        if(criteriaDTONew!=null){
            response.put("success", true);
            response.put("criteria",criteriaDTONew);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("success", false);
            response.put("criteria", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/criteria/getAll")
    public ResponseEntity<List<CriteriaDTO>> getAllSemesters() {
        List<CriteriaDTO> semesters = criteriaService.findAll();
        return ResponseEntity.ok(semesters);
    }
    @PostMapping("/manager1/criteria/update")
    public ResponseEntity<CriteriaDTO> update(@RequestBody CriteriaDTO criteriaDTO){
        System.out.println("update criteria"+criteriaDTO);
        CriteriaDTO criteriaDTO1=criteriaService.addCriteria(criteriaDTO);

        return ResponseEntity.ok(criteriaDTO1);
    }
    @GetMapping("/criteria/getByType/{criteriaTypeId}")
    public ResponseEntity<List<CriteriaDTO>> findByCriteriaTypeId(
            @PathVariable("criteriaTypeId") int criteriaTypeId
    ) {
        List<CriteriaDTO> list = criteriaService.findByCriteriaTypeId(criteriaTypeId);
        return ResponseEntity.ok(list);
    }
}

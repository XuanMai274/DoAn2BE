package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaTypeDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaTypeEntity;

import java.util.List;

public interface CriteriaTypeService {
    CriteriaTypeDTO addCriteriaType(CriteriaTypeDTO criteriaTypeDTO);
    List<CriteriaTypeDTO> findAll();
    //CriteriaTypeDTO updateCriteriaType(CriteriaTypeDTO criteriaTypeDTO);
}

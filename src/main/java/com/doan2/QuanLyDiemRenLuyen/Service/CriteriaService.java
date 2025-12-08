package com.doan2.QuanLyDiemRenLuyen.Service;


import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaDTO;

import java.util.List;

public interface CriteriaService {
    CriteriaDTO addCriteria(CriteriaDTO criteriaDTO);
    List<CriteriaDTO> findAll();
    List<CriteriaDTO> findByCriteriaTypeId(int criteriaTypeId);
}

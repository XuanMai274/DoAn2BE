package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CriteriaDTO {
    private int criteriaId;
    private String criteriaName;
    private String criteriaDetail;
    private int maxScore;

    // Quan hệ
    private CriteriaTypeDTO criteriaTypeEntity;
    private List<ConductFormDetailDTO> conductFormDetailEntityList;
}
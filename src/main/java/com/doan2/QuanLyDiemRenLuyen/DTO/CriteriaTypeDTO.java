package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CriteriaTypeDTO {
    private int criteriaTypeId;
    private String criteriaTypeName;
    private int maxScore;
    private Boolean isActive;
    // Quan hệ
    private List<CriteriaDTO> criteriaEntityList;
}

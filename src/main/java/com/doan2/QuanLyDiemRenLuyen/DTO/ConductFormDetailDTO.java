package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConductFormDetailDTO {
    private int conductFormDetailId;
    private int selfScore;
    private int classMonitorScore;
    private int staffScore;
    private String comment;
    private String file;
    private String tempId;
    private String publicId;
    // Quan hệ
    private ConductFormDTO conductForm;
    private CriteriaDTO criteria;
}
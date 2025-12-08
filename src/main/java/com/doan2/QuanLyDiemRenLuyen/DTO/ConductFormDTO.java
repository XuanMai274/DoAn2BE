package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ConductFormDTO {
    private int conductFormId;
    private int totalStudentScore;
    private int classMonitorScore;
    private int staffScore;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updatedDate;
    private int managerAccept;
    // Quan hệ
    private SemesterDTO semester;
    private StudentDTO student;
    private List<ConductFormDetailDTO> conductFormDetailList;
}
package com.doan2.QuanLyDiemRenLuyen.DTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SemesterDTO {
    private int semesterId;
    private String semesterName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String year;
    private boolean isOpen;
    private LocalDate evaluationStartDate;
    private LocalDate evaluationEndDate;
    private List<Integer> conductFormIds; // chỉ lưu danh sách ID để tránh vòng lặp khi mapping
}
package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacultySemesterDTO {
    private FacultyDTO facultyDTO;
    private SemesterDTO semesterDTO;
    private LocalDate evaluationStartDate;
    private LocalDate evaluationEndDate;
    private boolean isOpen;
}

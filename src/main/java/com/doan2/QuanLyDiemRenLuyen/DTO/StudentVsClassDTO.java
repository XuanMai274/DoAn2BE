package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentVsClassDTO {
    private SemesterDTO semesterName;  // Học kỳ
    private int studentScore;     // Điểm của sinh viên
    private double classAverage;  // Điểm trung bình lớp
}

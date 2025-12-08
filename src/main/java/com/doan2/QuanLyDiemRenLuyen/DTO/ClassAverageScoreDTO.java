package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ClassAverageScoreDTO {
    private int classId;
    private String className;
    private double averageScore;
}

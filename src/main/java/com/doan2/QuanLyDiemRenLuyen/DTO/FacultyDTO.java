package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FacultyDTO {
    private int facultyId;
    private String facultyName;

    // Quan hệ
    private List<ClassDTO> classList;
    private List<ManagerDTO> managerEntityList;
}
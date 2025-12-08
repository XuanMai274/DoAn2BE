package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ClassDTO {
    private int classId;
    private String className;

    private FacultyDTO facultyEntity;
    private List<StudentDTO> studentEntityList;
}

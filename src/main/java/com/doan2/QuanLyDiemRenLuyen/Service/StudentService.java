package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;

import java.util.List;

public interface StudentService {
    StudentDTO findByAccountId(int id);
    StudentDTO findByUsername(String username);
    List<StudentDTO> findStudentsSubmittedConductForm(int semester_id, int class_id);
    List<StudentDTO> findStudentsNotSubmittedConductForm(int semester_id, int class_id);

}

package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.SemesterDTO;

import java.util.List;

public interface SemesterService {
    SemesterDTO addSemester(SemesterDTO semesterDTO);
    List<SemesterDTO> findAll();
    List<SemesterDTO> findByIsOpenTrue();
    SemesterDTO CreateBatch(SemesterDTO semesterDTO);
    List<SemesterDTO> findSemesterOpened();
    List<SemesterDTO> availableSemesters();
    SemesterDTO update(SemesterDTO semesterDTO);
}

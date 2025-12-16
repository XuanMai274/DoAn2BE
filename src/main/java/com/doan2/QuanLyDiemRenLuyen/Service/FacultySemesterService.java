package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultySemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.SemesterDTO;

import java.util.List;

public interface FacultySemesterService {
    public FacultySemesterDTO createBatch(FacultySemesterDTO dto);
    public List<FacultySemesterDTO> findByIsOpenTrue(int facultyId);
    public List<FacultySemesterDTO> findByIsOpenForStudent(int facultyId,int studentId);
    public List<SemesterDTO> availableSemesters(int facultyId);
    public List<FacultySemesterDTO> findSemesterOpened(int facultyId);
    public FacultySemesterDTO updateBatch(FacultySemesterDTO dto);
}

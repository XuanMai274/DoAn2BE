package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.ClassAverageScoreDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ConductFormDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentVsClassDTO;

import java.util.List;

public interface ConductFormService {
    ConductFormDTO addConductForm(ConductFormDTO conductFormDTO);
    List<ConductFormDTO> findByStudentId(int studentId);
    ConductFormDTO findByConductFormId(int conductFormId);
    List<ConductFormDTO> findByClassAndSemester(int classId,int semester_id);
    ConductFormDTO updateManager(ConductFormDTO conductFormDTO);
    ConductFormDTO findByOneStudentId(int studentId);
    ConductFormDTO findByStudentAndOrderByCreateAtDesc(int studentId);
    List<ConductFormDTO> findAllConductFormByStudent(int student);
    List<StudentVsClassDTO> compareStudentVsClass(int studentId);
    StudentVsClassDTO compareLatestSemester(int studentId);
    public List<ClassAverageScoreDTO> getAverageScoreByFaculty(int facultyId);
}

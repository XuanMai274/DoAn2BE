package com.doan2.QuanLyDiemRenLuyen.Mapper;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.FacultySemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.SemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultySemesterEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.SemesterEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FacultySemesterMapper {

    public FacultySemesterDTO toDTO(FacultySemesterEntity entity) {
        if (entity == null) return null;

        FacultySemesterDTO dto = new FacultySemesterDTO();

        /* ----- MAP FACULTY (CHỈ LẤY ID) ----- */
        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setFacultyId(entity.getFaculty().getFacultyId());

        dto.setFacultyDTO(facultyDTO);

        /* ----- MAP SEMESTER ----- */
        dto.setSemesterDTO(toSemesterDTO(entity.getSemester()));

        /* ----- MAP CÁC TRƯỜNG KHÁC ----- */
        dto.setEvaluationStartDate(entity.getEvaluationStartDate());
        dto.setEvaluationEndDate(entity.getEvaluationEndDate());
        dto.setOpen(entity.isOpen());

        return dto;
    }

    public FacultySemesterEntity toEntity(FacultySemesterDTO dto) {
        if (dto == null) return null;

        FacultySemesterEntity entity = new FacultySemesterEntity();

        /* ----- MAP FACULTY CHỈ LẤY ID ----- */
        FacultyEntity facultyEntity = new FacultyEntity();
        facultyEntity.setFacultyId(dto.getFacultyDTO().getFacultyId());
        entity.setFaculty(facultyEntity);

        /* ----- MAP SEMESTER (CHỈ LẤY ID) ----- */
        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setSemesterId(dto.getSemesterDTO().getSemesterId());
        entity.setSemester(semesterEntity);

        /* ----- MAP CÁC TRƯỜNG KHÁC ----- */
        entity.setEvaluationStartDate(dto.getEvaluationStartDate());
        entity.setEvaluationEndDate(dto.getEvaluationEndDate());
        entity.setOpen(dto.isOpen());

        return entity;
    }

    private SemesterDTO toSemesterDTO(SemesterEntity s) {
        if (s == null) return null;

        SemesterDTO dto = new SemesterDTO();

        dto.setSemesterId(s.getSemesterId());
        dto.setSemesterName(s.getSemesterName());
        dto.setStartDate(s.getStartDate());
        dto.setEndDate(s.getEndDate());
        dto.setYear(s.getYear());
        dto.setOpen(s.isOpen());
        dto.setEvaluationStartDate(s.getEvaluationStartDate());
        dto.setEvaluationEndDate(s.getEvaluationEndDate());

        // xử lý danh sách conductFormIds
        if (s.getConductFormEntityList() != null) {
            dto.setConductFormIds(
                    s.getConductFormEntityList()
                            .stream()
                            .map(cf -> cf.getConductFormId())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}


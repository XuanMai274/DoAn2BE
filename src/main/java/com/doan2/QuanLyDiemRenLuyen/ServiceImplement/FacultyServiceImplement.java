package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.FacultyMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.FacultyRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacultyServiceImplement implements FacultyService {
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    FacultyMapper facultyMapper;
    @Override
    public List<FacultyDTO> findAll() {
        List<FacultyEntity> facultyEntities= facultyRepository.findAll();
        List<FacultyDTO> facultyDTOS=new ArrayList<>();
        for(FacultyEntity f:facultyEntities){
            facultyDTOS.add(facultyMapper.toDTO(f));
        }
        return facultyDTOS;
    }
}

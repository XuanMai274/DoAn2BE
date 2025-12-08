package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.ClassDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ClassEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.ClassMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.ClassRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ClassServiceImplement implements ClassService {
    @Autowired ClassRepository classRepository;
    @Autowired
    ClassMapper classMapper;
    @Override
    public List<ClassDTO> findAll(int faculty) {
        try{
            List<ClassEntity> classEntities= classRepository.findByFacultyId_facultyId(faculty);
            if(classEntities!=null){
                List<ClassDTO> classDTO=new ArrayList<>();
                for(ClassEntity c:classEntities){
                    classDTO.add(classMapper.toDTO(c));
                }
                return classDTO;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    @Override
    public List<ClassDTO> findByFacultyId(int facultyId) {
        List<ClassEntity> classEntities=classRepository.findByFacultyId_facultyId(facultyId);
        if(classEntities!=null){
            List<ClassDTO> classDTO= new ArrayList<>();
            for(ClassEntity c:classEntities){
                classDTO.add(classMapper.toDTO(c));
            }
            return classDTO;
        }
        return List.of();
    }
}

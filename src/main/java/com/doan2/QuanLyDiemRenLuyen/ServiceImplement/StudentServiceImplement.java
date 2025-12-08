package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.StudentMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.StudentRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImplement implements StudentService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StudentMapper studentMapper;
    @Override
    public StudentDTO findByAccountId(int id) {
        StudentEntity student=studentRepository.findByAccountId(id);
        if(student!=null){
            StudentDTO studentDTO=new StudentDTO();
            studentDTO.setFullname(student.getFullname());
            studentDTO.setIsClassMonitor(student.getIsClassMonitor());
            return studentDTO;
        }
        else{
            return  null;
        }
    }

    @Override
    public StudentDTO findByUsername(String username) {
        StudentEntity student=studentRepository.findByUserName(username);
        if(student!=null){
            StudentDTO studentDTO=new StudentDTO();
            studentDTO.setFullname(student.getFullname());
            studentDTO.setIsClassMonitor(student.getIsClassMonitor());
            return studentDTO;
        }
        else{
            return  null;
        }
    }

    @Override
    public List<StudentDTO> findStudentsSubmittedConductForm(int semester_id, int class_id) {
        List<StudentEntity> studentEntities=studentRepository.findStudentsSubmittedConductForm(semester_id,class_id);
        if(studentEntities!=null){
            List<StudentDTO> studentDTOS= new ArrayList<>();
            for (StudentEntity s: studentEntities){
                studentDTOS.add(studentMapper.toDTO(s));
            }
            return studentDTOS;
        }
        return List.of();
    }

    @Override
    public List<StudentDTO> findStudentsNotSubmittedConductForm(int semester_id, int class_id) {
        List<StudentEntity> studentEntities=studentRepository.findStudentsNotSubmittedConductForm(semester_id,class_id);
        if(studentEntities!=null){
            List<StudentDTO> studentDTOS= new ArrayList<>();
            for (StudentEntity s: studentEntities){
                studentDTOS.add(studentMapper.toDTO(s));
            }
            return studentDTOS;
        }
        return List.of();
    }
}

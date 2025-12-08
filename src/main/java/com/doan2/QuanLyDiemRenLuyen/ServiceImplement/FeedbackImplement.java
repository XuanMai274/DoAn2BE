package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.FeedbackDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ConductFormEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.FeedbackEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.StudentEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.FeedbackMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.ConductFormRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.FeedbackRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.ManagerRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.StudentRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackImplement  implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ConductFormRepository conductFormRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public FeedbackDTO create(FeedbackDTO dto) {

        FeedbackEntity entity;
        if (dto.getFeedbackId() != 0) {
            entity = feedbackRepository.findByFeedbackId(dto.getFeedbackId());
            entity.setUpdatedDate(LocalDateTime.now());
            if(dto.getResponseContent()!=null){
                entity.setResponseContent(dto.getResponseContent());
                entity.setResponse(true);
            }
        } else {
            entity = new FeedbackEntity();
            entity.setCreateAt(LocalDateTime.now());
        }

        // Map phần text
        entity.setContent(dto.getContent());
        entity.setImage(dto.getImage());
        entity.setGmail(dto.getGmail());
        if (dto.getStudentDTO() != null) {
            StudentEntity student =
                    studentRepository.findByStudentId(dto.getStudentDTO().getStudentId());
            entity.setStudentEntity(student);
        }
        if (dto.getConductFormDTO() != null) {
            ConductFormEntity conductForm =
                    conductFormRepository.findByConductFormId(dto.getConductFormDTO().getConductFormId());
            entity.setConductFormEntity(conductForm);
        }
        if (dto.getManagerDTO() != null) {
            ManagerEntity manager =
                    managerRepository.findByManagerId(dto.getManagerDTO().getManagerId());
            entity.setManagerEntity(manager);
        }
        FeedbackEntity saved = feedbackRepository.save(entity);
        return feedbackMapper.toDTO(saved);
    }

    @Override
    public List<FeedbackDTO> findAllByStudent(int studentId) {
        List<FeedbackEntity> feedbackEntities=feedbackRepository.findByStudentEntity_studentId(studentId);
        if(feedbackEntities!=null){
            List<FeedbackDTO> feedbackDTOS=new ArrayList<>();
            for(FeedbackEntity f: feedbackEntities){
                feedbackDTOS.add(feedbackMapper.toDTO(f));
            }
            return feedbackDTOS;
        }
        return List.of();
    }

    @Override
    public List<FeedbackDTO> findAll(int managerId) {
        List<FeedbackEntity> feedbackEntities=feedbackRepository.getFeedbackByManager(managerId);
        List<FeedbackDTO> feedbackDTOS = new ArrayList<>();
        for(FeedbackEntity f: feedbackEntities){
            feedbackDTOS.add(feedbackMapper.toDTO(f));
        }
        return feedbackDTOS;
    }

    @Override
    public FeedbackDTO update(FeedbackDTO feedbackDTO) {
        // lấy lên feedback hien tại
        FeedbackEntity feedbackEntity=feedbackRepository.findByFeedbackId(feedbackDTO.getFeedbackId());
        ManagerEntity managerEntity=managerRepository.findByManagerId(feedbackDTO.getManagerDTO().getManagerId());
        if(feedbackEntity!=null){
            feedbackEntity.setResponseContent(feedbackDTO.getResponseContent());
            feedbackEntity.setResponse(true);
            feedbackEntity.setUpdatedDate(LocalDateTime.now());
            if(managerEntity!=null){
                feedbackEntity.setManagerEntity(managerEntity);
            }
            feedbackRepository.save(feedbackEntity);
            return feedbackMapper.toDTO(feedbackEntity);
        }

        return null;
    }
}

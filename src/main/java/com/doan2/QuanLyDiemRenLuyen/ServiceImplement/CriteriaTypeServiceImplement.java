package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.CriteriaTypeDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.CriteriaTypeEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.CriteriaTypeMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.CriteriaRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.CriteriaTypeRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.CriteriaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CriteriaTypeServiceImplement implements CriteriaTypeService {
    @Autowired
    CriteriaTypeRepository criteriaTypeRepository;
    @Autowired
    CriteriaTypeMapper criteriaTypeMapper;
    @Override
    public CriteriaTypeDTO addCriteriaType(CriteriaTypeDTO criteriaTypeDTO) {
        try {
            if(criteriaTypeDTO.getCriteriaTypeId()!=0){
                CriteriaTypeEntity criteriaTypeEntity=criteriaTypeRepository.findByCriteriaTypeId(criteriaTypeDTO.getCriteriaTypeId());
                if(criteriaTypeEntity!=null){
                    criteriaTypeRepository.save(criteriaTypeMapper.toEntity(criteriaTypeDTO));
                    return criteriaTypeMapper.toDTO(criteriaTypeEntity);
                }
            }else {
                // chuyển sang entity
                CriteriaTypeEntity criteriaTypeEntity=criteriaTypeMapper.toEntity(criteriaTypeDTO);
                // lưu vào Db
                CriteriaTypeEntity savedEntity=criteriaTypeRepository.save(criteriaTypeEntity);
                // chuyển ngược lại về DTO
                return criteriaTypeMapper.toDTO(savedEntity);
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lưu CriteriaEntity: " + e.getMessage());
            // Có thể ném lại exception để controller xử lý
            throw new RuntimeException("Không thể thêm loại tiêu chí. Vui lòng thử lại sau!", e);
        }
        return null;
    }

    @Override
    public List<CriteriaTypeDTO> findAll() {
        List<CriteriaTypeEntity> criteriaTypeEntities=criteriaTypeRepository.findByIsActiveTrue();
        List<CriteriaTypeDTO> criteriaTypeDTOS=new ArrayList<>();
        for(CriteriaTypeEntity c:criteriaTypeEntities){
            criteriaTypeDTOS.add(criteriaTypeMapper.toDTO(c));
        }
        return criteriaTypeDTOS;
    }
}

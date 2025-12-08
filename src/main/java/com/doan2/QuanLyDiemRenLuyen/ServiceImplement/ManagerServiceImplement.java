package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.ChangePasswordDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerUpdateDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.AccountEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.ManagerMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.AccountRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.ManagerRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerServiceImplement implements ManagerService {
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    ManagerMapper managerMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ManagerDTO findByAccountId(int id) {
        try{
            ManagerEntity managerEntity= managerRepository.findByAccountId(id);
            return managerMapper.toDTO(managerEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

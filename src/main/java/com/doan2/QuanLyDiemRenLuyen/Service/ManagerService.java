package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.ChangePasswordDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerUpdateDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;

public interface ManagerService {
    ManagerDTO findByAccountId(int id);
//    public ManagerEntity updateProfile(int managerId, ManagerUpdateDTO dto);
//    public boolean changePassword(ChangePasswordDTO dto);
}

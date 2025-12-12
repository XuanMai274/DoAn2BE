package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.ChangePasswordRequest;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerUpdateDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManagerService {
    ManagerDTO findByAccountId(int id);
    public ManagerDTO updateProfile(ManagerDTO managerDTO, MultipartFile avt);
    public ManagerDTO getProfile(int managerId);
    public boolean changePassword(ChangePasswordRequest req);
    public List<ManagerDTO> findAllManager();
    public ManagerDTO createManager(ManagerDTO dto);
    public ManagerDTO updateManager( ManagerDTO dto);
    public void resetManagerPasswordAndSendEmail(int managerId);
    public String buildResetEmailHtml(String fullName, String tempPassword);
}

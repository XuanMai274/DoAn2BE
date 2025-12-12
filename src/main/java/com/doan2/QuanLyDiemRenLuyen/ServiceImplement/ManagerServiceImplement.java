package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doan2.QuanLyDiemRenLuyen.DTO.ChangePasswordRequest;
import com.doan2.QuanLyDiemRenLuyen.DTO.FacultyDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerUpdateDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.AccountEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.ManagerEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.RoleEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.ManagerMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.AccountRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.FacultyRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.ManagerRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.EmailService;
import com.doan2.QuanLyDiemRenLuyen.Service.ManagerService;
import com.doan2.QuanLyDiemRenLuyen.Utill.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    EmailService emailService;
    @Override
    public ManagerDTO findByAccountId(int id) {
        try {
            ManagerEntity managerEntity = managerRepository.findByAccountId(id);
            return managerMapper.toDTO(managerEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ManagerDTO updateProfile(ManagerDTO dto, MultipartFile avatarFile) {
        ManagerEntity manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quản lý"));

        // ===========================
        // 1) UPLOAD ẢNH
        // ===========================
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        avatarFile.getBytes(),
                        ObjectUtils.asMap("folder", "manager/avatar")
                );

                String url = uploadResult.get("secure_url").toString();

                // Set vào DTO
                dto.setAvatarUrl(url);

                // Set vào entity
                manager.setAvatarUrl(url);

            } catch (Exception e) {
                throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
            }
        }

        // ===========================
        // 2) CẬP NHẬT THÔNG TIN CÁ NHÂN
        // ===========================
        manager.setFullname(dto.getFullname());
        manager.setEmail(dto.getEmail());
        manager.setPhoneNumber(dto.getPhoneNumber());
        manager.setStatus(dto.getStatus());

        // update khoa nếu có
//        if (dto.getFacultyEntity() != null) {
//            FacultyEntity faculty = facultyRepository.findById(dto.getFacultyEntity().getFacultyId())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
//
//            manager.setFacultyEntity(faculty);
//        }

        managerRepository.save(manager);

        return managerMapper.toDTO(manager);
    }


    @Override
    public ManagerDTO getProfile(int managerId) {
        ManagerEntity managerEntity = managerRepository.findByManagerId(managerId);
        if (managerEntity != null) {
            return managerMapper.toDTO(managerEntity);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean changePassword(ChangePasswordRequest req) {
        AccountEntity account = accountRepository.findById(req.getAccountId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // kiểm tra mật khẩu hiện tại
        if (!encoder.matches(req.getCurrentPassword(), account.getPassword())) {
            return false; // trả về sai mật khẩu
        }

        // mã hóa mật khẩu mới
        String newEncodedPass = encoder.encode(req.getNewPassword());
        account.setPassword(newEncodedPass);

        accountRepository.save(account);
        return true;
    }

    @Override
    public List<ManagerDTO> findAllManager() {
        List<ManagerEntity> managerEntities = managerRepository.findByAllManager();
        if (managerEntities != null) {
            List<ManagerDTO> managerDTOS = new ArrayList<>();
            for (ManagerEntity m : managerEntities) {
                managerDTOS.add(managerMapper.toDTO(m));
            }
            return managerDTOS;
        }
        return null;
    }

    @Override
    public ManagerDTO createManager(ManagerDTO dto) {

        // 1. Tạo Account mới
        AccountEntity account = new AccountEntity();
        account.setUsername(dto.getAccountEntity().getUsername());
        String encodedPassword = passwordEncoder.encode(dto.getAccountEntity().getPassword());
        account.setPassword(encodedPassword);
        account.setEnable(true);

        // Gán roleId = 2
        RoleEntity role = new RoleEntity();
        role.setRoleId(2);
        account.setRole(role);

        account = accountRepository.save(account);

        // 2. Map Manager
        ManagerEntity manager = new ManagerEntity();
        manager.setFullname(dto.getFullname());
        manager.setEmail(dto.getEmail());
        manager.setPhoneNumber(dto.getPhoneNumber());
        manager.setStatus(dto.getStatus());
        manager.setAvatarUrl(dto.getAvatarUrl());
        manager.setAccountEntity(account);
        manager.setStatus("ACTIVE");
        if (dto.getFacultyEntity() != null) {
            FacultyEntity faculty = facultyRepository
                    .findById(dto.getFacultyEntity().getFacultyId())
                    .orElse(null);
            manager.setFacultyEntity(faculty);
        }

        managerRepository.save(manager);

        return managerMapper.toDTO(manager);
    }

    @Override
    public ManagerDTO updateManager(ManagerDTO dto) {
        ManagerEntity manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        // ========== Update Faculty (nếu có) ==========
        if (dto.getFacultyEntity() != null && dto.getFacultyEntity().getFacultyId() != 0) {
            FacultyEntity faculty = facultyRepository
                    .findById(dto.getFacultyEntity().getFacultyId())
                    .orElse(null);
            manager.setFacultyEntity(faculty);
        }

        // ========== Update Account.enable và password ==========
        AccountEntity account = manager.getAccountEntity();

        if (dto.getAccountEntity() != null) {

            // update enable
            if (dto.getAccountEntity().getEnable() != null) {
                account.setEnable(dto.getAccountEntity().getEnable());

                // cập nhật status cho Manager
                if (dto.getAccountEntity().getEnable()) {
                    manager.setStatus("ACTIVE");
                } else {
                    manager.setStatus("INACTIVE");
                }
            }

            accountRepository.save(account);
            managerRepository.save(manager);

            return managerMapper.toDTO(manager);
        }

        return dto;
    }

    // phần đặt lại mật khẩu cho quản lý

    @Transactional
    @Override
    public void resetManagerPasswordAndSendEmail(int managerId) {
        ManagerEntity manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found: " + managerId));

        AccountEntity account = manager.getAccountEntity();
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản cho manager id=" + managerId);
        }

        // 1. Tạo mật khẩu tạm
        String tempPassword = PasswordGenerator.random(8); // 8 ký tự, bạn có thể đổi

        // 2. Hash mật khẩu
        String encoded = passwordEncoder.encode(tempPassword);
        account.setPassword(encoded);
        accountRepository.save(account); // lưu mật khẩu mới đã hash

        // 3. Gửi email HTML
        String subject = "Mật khẩu tạm thời - Quản lý hệ thống";
        String html = buildResetEmailHtml(manager.getFullname(), tempPassword);
        emailService.sendEmail(manager.getEmail(), subject, html);

        // 4. (tuỳ chọn) ghi log / notification
    }
    @Override
    public String buildResetEmailHtml(String fullName, String tempPassword) {
        // đơn giản, bạn có thể thêm logo, style, footer
        String content = "<div style='font-family: Arial, sans-serif; color: #333; line-height:1.6;'>"
                + "<h3 style='color:#2c3e50'>Mật khẩu tạm thời</h3>"
                + "<p>Chào <strong>" + (fullName == null ? "" : fullName) + "</strong>,</p>"
                + "<p>Quản trị hệ thống đã đặt lại mật khẩu cho tài khoản của bạn.</p>"
                + "<p><strong>Mật khẩu tạm thời:</strong></p>"
                + "<p style='font-size:18px; font-weight:700; background:#f4f6f8; padding:10px; display:inline-block;'>"
                + tempPassword + "</p>"
                + "<p>Vui lòng đăng nhập và <strong>đổi mật khẩu ngay</strong> để bảo mật tài khoản.</p>"
                + "<hr/>"
                + "<p>Trân trọng,<br/>Ban Quản Trị Hệ Thống</p>"
                + "</div>";
        return content;
    }
}



package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.*;
import com.doan2.QuanLyDiemRenLuyen.Service.CloudinaryService;
import com.doan2.QuanLyDiemRenLuyen.Service.ConductFormService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;


@RestController
public class ConductFormAPI {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ConductFormService conductFormService;

    @PostMapping(value = "/conductForm/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createConductForm(
            @RequestPart("conductForm") ConductFormDTO dto,
            @RequestPart(value = "detailFiles", required = false) List<MultipartFile> detailFiles,
            @RequestPart(value = "detailMeta", required = false) List<DetailMetaDTO> detailMetaList
    ) {
       // List<String> uploadedPublicIds = new ArrayList<>();
        try {
            //Kiểm tra đăng nhập và lấy id của người dùng hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
                return ResponseEntity.status(401).body("Chưa đăng nhập hoặc token không hợp lệ");
            }

            CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
            int studentId = user.getAccountEntity().getStudentEntity().getStudentId();

            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setStudentId(studentId);
            dto.setStudent(studentDTO);

            // Nếu không có file thì lưu luôn
            if (detailFiles == null || detailFiles.isEmpty()) {
                ConductFormDTO saved = conductFormService.addConductForm(dto);
                return ResponseEntity.ok(saved);
            }

            // Kiểm tra số lượng file và meta có khớp không
            if (detailMetaList == null || detailMetaList.size() != detailFiles.size()) {
                return ResponseEntity.badRequest().body("Số lượng detailFiles và detailMeta không khớp");
            }

            //Gắn file tương ứng với meta
            for (int i = 0; i < detailFiles.size(); i++) {
                MultipartFile file = detailFiles.get(i);
                DetailMetaDTO meta = detailMetaList.get(i);

                if (meta.getTempId() == null) {
                    return ResponseEntity.badRequest().body("Thiếu tempId cho meta index " + i);
                }

                // Upload ảnh
                Map<String, String> uploadRes = cloudinaryService.uploadImage(file);
                String url = uploadRes.get("url");
                String publicId = uploadRes.get("public_id");
                System.out.println("Public_id nhận được: "+publicId);
              //  uploadedPublicIds.add(publicId);

                // Gán ảnh vào đúng detail
                if (dto.getConductFormDetailList() != null) {
                    for (ConductFormDetailDTO d : dto.getConductFormDetailList()) {
                        if (meta.getTempId().equals(d.getTempId())) {
                            d.setFile(url);
                            d.setPublicId(publicId);
                            break;
                        }
                    }
                }
            }
            // set thời gian hiện tại
            dto.setCreateAt(LocalDateTime.now());
            //Lưu phiếu rèn luyện
            ConductFormDTO savedForm = conductFormService.addConductForm(dto);
            return ResponseEntity.ok(savedForm);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi xử lý: " + e.getMessage());
        }
    }
    @GetMapping("/conductForm/findAll")
    ResponseEntity<Map<String,Object>> findALlByStudentId(){
        // kiểm tra đăng nhập và lấy id của người dùng hiện tại
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"));
        }
        CustomeUserDetails customeUserDetails=(CustomeUserDetails) userDetails;
        int id=customeUserDetails.getAccountEntity().getStudentEntity().getStudentId();
       // int faculty=customeUserDetails.getAccountEntity().getStudentEntity().getClassId().getFacultyId().getFacultyId();
        List<ConductFormDTO> conductFormDTOS=conductFormService.findByStudentId(id);
        Map<String,Object> response=new HashMap<>();
        if(conductFormDTOS!=null){
            response.put("success", true);
            response.put("conductForms",conductFormDTOS);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("success", false);
            response.put("conductForms", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/conductForm/Detail/{id}")
    ResponseEntity<Map<String,Object>> findByConductFormId(@PathVariable("id")int id){
        ConductFormDTO conductFormDTO= conductFormService.findByConductFormId(id);
        Map<String,Object> response= new HashMap<>();
        if(conductFormDTO!=null){
            response.put("success", true);
            response.put("conductForm",conductFormDTO);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("success", false);
            response.put("conductForm", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PostMapping(value = "/conductForm/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateConductForm(
            @PathVariable("id") int conductFormId,
            @RequestPart("conductForm") ConductFormDTO dto,
            @RequestPart(value = "detailFiles", required = false) List<MultipartFile> detailFiles,
            @RequestPart(value = "detailMeta", required = false) List<DetailMetaDTO> detailMetaList) {

        try {
            //1. KIỂM TRA QUYỀN
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof CustomeUserDetails)) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            CustomeUserDetails user = (CustomeUserDetails) auth.getPrincipal();
            int currentStudentId = user.getAccountEntity().getStudentEntity().getStudentId();

            ConductFormDTO existingForm = conductFormService.findByConductFormId(conductFormId);
            if (existingForm == null) return ResponseEntity.notFound().build();
            if (existingForm.getStudent() == null ||
                    existingForm.getStudent().getStudentId() != currentStudentId) {
                return ResponseEntity.status(403).body("Bạn không có quyền sửa phiếu này");
            }

            //2. LẤY BẢN GỐC TỪ DB ĐỂ GIỮ NGUYÊN CÁC TRƯỜNG KHÔNG THAY ĐỔI
            ConductFormDTO formToUpdate = conductFormService.findByConductFormId(conductFormId);
            if (formToUpdate == null) return ResponseEntity.notFound().build();

            // CẬP NHẬT CÁC TRƯỜNG CHUNG
            formToUpdate.setSemester(dto.getSemester());
            formToUpdate.setTotalStudentScore(dto.getTotalStudentScore());
            formToUpdate.setClassMonitorScore(dto.getClassMonitorScore());
            formToUpdate.setStaffScore(dto.getStaffScore());
            formToUpdate.setStatus(dto.getStatus());
            //CẬP NHẬT CÁC TRƯỜNG TRONG DETAIL TỪ DTO GỬI LÊN
            if (dto.getConductFormDetailList() != null) {
                for (ConductFormDetailDTO updatedDetail : dto.getConductFormDetailList()) {
                    if (updatedDetail.getCriteria() == null || updatedDetail.getCriteria().getCriteriaId() == 0) {
                        continue;
                    }

                    ConductFormDetailDTO targetDetail = formToUpdate.getConductFormDetailList().stream()
                            .filter(d -> d.getCriteria() != null &&
                                    updatedDetail.getCriteria().getCriteriaId()==(d.getCriteria().getCriteriaId()))
                            .findFirst()
                            .orElse(null);

                    if (targetDetail != null) {
                        // CẬP NHẬT TẤT CẢ CÁC TRƯỜNG CẦN THIẾT
                        targetDetail.setSelfScore(updatedDetail.getSelfScore());
                        targetDetail.setComment(updatedDetail.getComment());
                        targetDetail.setClassMonitorScore(updatedDetail.getClassMonitorScore());
                        targetDetail.setStaffScore(updatedDetail.getStaffScore());
                    }
                }
            }
            //3. CHỈ XỬ LÝ FILE QUA detailMetaList – KHÔNG ĐỂ DTO GHI ĐÈ
            if (detailMetaList != null && !detailMetaList.isEmpty()) {
                int fileIndex = 0;

                for (DetailMetaDTO meta : detailMetaList) {
                    if (meta.getCriteriaId() == null) {
                        return ResponseEntity.badRequest().body("Thiếu criteriaId");
                    }
                    // Lấy lên tất cả các tiêu chí cần xử lý file với điều kiện trong meta data có tồn tại mã tiêu chí cần upload
                    ConductFormDetailDTO targetDetail = formToUpdate.getConductFormDetailList().stream()
                            .filter(d -> d.getCriteria() != null &&
                                    meta.getCriteriaId().equals(d.getCriteria().getCriteriaId()))
                            .findFirst()
                            .orElse(null);

                    if (targetDetail == null) continue;

                    // XÓA FILE CŨ
                    if (Boolean.TRUE.equals(meta.isDeleteOldFile())) {
                        if (targetDetail.getPublicId() != null) {
                            cloudinaryService.deleteImage(targetDetail.getPublicId());
                            targetDetail.setPublicId(null);
                            targetDetail.setFile(null);
                        }
                        continue;
                    }

                    // UPLOAD FILE MỚI
                    if (detailFiles != null && fileIndex < detailFiles.size()) {
                        MultipartFile newFile = detailFiles.get(fileIndex);

                        if (targetDetail.getPublicId() != null) {
                            cloudinaryService.deleteImage(targetDetail.getPublicId());
                        }

                        Map<String, String> result = cloudinaryService.uploadImage(newFile);
                        targetDetail.setFile(result.get("url"));
                        targetDetail.setPublicId(result.get("public_id"));

                        fileIndex++;
                    }
                }
            }
            // → Các detail KHÔNG có trong detailMetaList → giữ nguyên hoàn toàn (file + publicId)

            // 4. LƯU LẠI
            ConductFormDTO saved = conductFormService.addConductForm(formToUpdate);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }
    @GetMapping("/manager/conductForm/{classId}/{semesterId}")
    public ResponseEntity<List<ConductFormDTO>> findByClassAndSemester(@PathVariable("classId") int classId, @PathVariable("semesterId")int semesterId){
        try{
            List<ConductFormDTO> conductFormDTOList=conductFormService.findByClassAndSemester(classId,semesterId);
            if(conductFormDTOList!=null){
                return ResponseEntity.ok(conductFormDTOList);
            }
            ResponseEntity.badRequest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @PostMapping("manager/conductForm/update")
    public ResponseEntity<ConductFormDTO> updateManager(@RequestBody ConductFormDTO conductFormDTO){
        try{
            // lấy thông tin của người quản lý hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
                return ResponseEntity.badRequest().body((ConductFormDTO) Collections.emptyList());
            }
            CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
            int managerId= user.getAccountEntity().getManagerEntity().getManagerId();
            conductFormDTO.setManagerAccept(managerId);
            ConductFormDTO conductFormDTO1=conductFormService.updateManager(conductFormDTO);
            if(conductFormDTO1!=null){
                return ResponseEntity.ok(conductFormDTO1);
            }
            ResponseEntity.badRequest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    //công tác sinh viên chấm điểm
    @PostMapping("manager1/conductForm/update")
    public ResponseEntity<ConductFormDTO> updateManager1(@RequestBody ConductFormDTO conductFormDTO){
        try{
            // lấy thông tin của người quản lý hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails)) {
                return ResponseEntity.badRequest().body((ConductFormDTO) Collections.emptyList());
            }
            CustomeUserDetails user = (CustomeUserDetails) authentication.getPrincipal();
            int managerId= user.getAccountEntity().getManagerEntity().getManagerId();
            conductFormDTO.setManagerAccept(managerId);
            ConductFormDTO conductFormDTO1=conductFormService.updateManager(conductFormDTO);
            if(conductFormDTO1!=null){
                return ResponseEntity.ok(conductFormDTO1);
            }
            ResponseEntity.badRequest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @GetMapping("manager/conductForm/studentId/{studentId}")
    public ResponseEntity<ConductFormDTO> findByStudentId(@PathVariable("studentId") int student){
        try{
            ConductFormDTO conductFormDTO= conductFormService.findByOneStudentId( student);
            if(conductFormDTO!=null){
                return ResponseEntity.ok(conductFormDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @GetMapping("student/dashboard/conductForm")
    public ResponseEntity<ConductFormDTO> findByStudentAndOrderByCreateAtDesc(){
        // lấy id của sinh viên hiện tại
        // kiểm tra đăng nhập và lấy id của người dùng hiện tại
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return null;
        }
        int id= ((CustomeUserDetails) userDetails).getAccountEntity().getStudentEntity().getStudentId();
        ConductFormDTO conductFormDTO=conductFormService.findByStudentAndOrderByCreateAtDesc(id);
        if(conductFormDTO!=null){
            return ResponseEntity.ok(conductFormDTO);
        }
        return null;
    }
    @GetMapping("student/dashboard/conductForms")
    public ResponseEntity<List<ConductFormDTO>> findAllConductFormByStudent(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return null;
        }
        int id= ((CustomeUserDetails) userDetails).getAccountEntity().getStudentEntity().getStudentId();
        List<ConductFormDTO> conductFormDTOS=conductFormService.findAllConductFormByStudent(id);
        if(conductFormDTOS!=null){
            return ResponseEntity.ok(conductFormDTOS);
        }
        return null;
    }
    // So sánh theo tất cả học kỳ
    @GetMapping("conductForm/compare/student")
    public List<StudentVsClassDTO> compareStudentVsClass() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return null;
        }
        int id= ((CustomeUserDetails) userDetails).getAccountEntity().getStudentEntity().getStudentId();
        return conductFormService.compareStudentVsClass(id);
    }

    // So sánh 1 học kỳ gần nhất
    @GetMapping("/conductForm/compare/student/latest")
    public StudentVsClassDTO compareLatestSemester() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomeUserDetails userDetails)) {
            return null;
        }
        int id= ((CustomeUserDetails) userDetails).getAccountEntity().getStudentEntity().getStudentId();
        return conductFormService.compareLatestSemester(id);
    }
}

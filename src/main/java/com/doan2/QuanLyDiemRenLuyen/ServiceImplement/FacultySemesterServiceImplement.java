package com.doan2.QuanLyDiemRenLuyen.ServiceImplement;

import com.doan2.QuanLyDiemRenLuyen.DTO.FacultySemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.SemesterDTO;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultyEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.FacultySemesterEntity;
import com.doan2.QuanLyDiemRenLuyen.Entity.SemesterEntity;
import com.doan2.QuanLyDiemRenLuyen.Mapper.FacultySemesterMapper;
import com.doan2.QuanLyDiemRenLuyen.Mapper.SemesterMapper;
import com.doan2.QuanLyDiemRenLuyen.Repository.FacultyRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.FacultySemesterRepository;
import com.doan2.QuanLyDiemRenLuyen.Repository.SemesterRepository;
import com.doan2.QuanLyDiemRenLuyen.Service.FacultySemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacultySemesterServiceImplement implements FacultySemesterService {
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    FacultySemesterRepository facultySemesterRepository;
    @Autowired
    FacultySemesterMapper facultySemesterMapper;
    @Autowired
    SemesterMapper semesterMapper;
    @Override
    public FacultySemesterDTO createBatch(FacultySemesterDTO dto) {
        FacultyEntity faculty = facultyRepository.findByFacultyId(dto.getFacultyDTO().getFacultyId());
        if(faculty==null){
            return null;
        }
        SemesterEntity semester = semesterRepository.findBySemesterId(dto.getSemesterDTO().getSemesterId());
        // Kiểm tra xem khoa này đã có bản ghi faculty_semester cho học kỳ này chưa
        FacultySemesterEntity fs = facultySemesterRepository
                .findByFacultyFacultyIdAndSemesterSemesterId(dto.getFacultyDTO().getFacultyId(), dto.getSemesterDTO().getSemesterId());

        if (fs == null) {
            fs = new FacultySemesterEntity();
            fs.setFaculty(faculty);
            fs.setSemester(semester);
        }

        fs.setEvaluationStartDate(dto.getEvaluationStartDate());
        fs.setEvaluationEndDate(dto.getEvaluationEndDate());
        fs.setOpen(true);

        FacultySemesterEntity saved = facultySemesterRepository.save(fs);

//        // Mapping thủ công (hoặc dùng mapper)
//        FacultySemesterDTO result = new FacultySemesterDTO();
//        result.(dto.getFacultyId());
//        result.setSemesterId(dto.getSemesterId());
//        result.setEvaluationStartDate(saved.getEvaluationStartDate());
//        result.setEvaluationEndDate(saved.getEvaluationEndDate());
//        result.setOpen(saved.isOpen());

        return facultySemesterMapper.toDTO(saved);
    }

    @Override
    public List<FacultySemesterDTO> findByIsOpenTrue(int facultyId) {
        List<FacultySemesterEntity> list = facultySemesterRepository
                .findOpenSemestersWithinEvaluationPeriod(facultyId, LocalDate.now());

        List<FacultySemesterDTO> result = new ArrayList<>();

        for (FacultySemesterEntity fs : list) {
            result.add(facultySemesterMapper.toDTO(fs));
        }
        return result;
    }

    @Override
    public List<SemesterDTO> availableSemesters(int facultyId) {
        List<SemesterEntity> list = facultySemesterRepository.findAvailableSemesters(facultyId);

        List<SemesterDTO> result = new ArrayList<>();
        for (SemesterEntity s : list) {
            result.add(semesterMapper.toDTO(s));
        }

        return result;
    }

    @Override
    public List<FacultySemesterDTO> findSemesterOpened(int facultyId) {
        List<FacultySemesterEntity> list = facultySemesterRepository.findSemesterClosed(facultyId);
        List<FacultySemesterDTO> result = new ArrayList<>();
        for (FacultySemesterEntity fs : list) {
            result.add(facultySemesterMapper.toDTO(fs));
        }
        return result;
    }

    @Override
    public FacultySemesterDTO updateBatch(FacultySemesterDTO dto) {
        // Lấy khoa
        FacultyEntity faculty = facultyRepository.findByFacultyId(dto.getFacultyDTO().getFacultyId());
        if (faculty == null) {
            return null;
        }

        // Lấy học kỳ
        SemesterEntity semester = semesterRepository.findBySemesterId(dto.getSemesterDTO().getSemesterId());
        if (semester == null) {
            return null;
        }

        // Lấy bản ghi FacultySemester hiện tại
        FacultySemesterEntity fs = facultySemesterRepository
                .findByFacultyFacultyIdAndSemesterSemesterId(faculty.getFacultyId(), semester.getSemesterId());
        // Cập nhật thời gian bắt đầu: nếu truyền null giữ nguyên
        if (dto.getEvaluationStartDate() != null) {
            fs.setEvaluationStartDate(dto.getEvaluationStartDate());
        }
        fs.setEvaluationEndDate(dto.getEvaluationEndDate());

        LocalDate today = LocalDate.now();
//       if(!dto.isOpen()){
//           fs.setEvaluationEndDate(today);
//       }else{
//
//       }
       // Cập nhật trạng thái isOpen: true nếu chưa hết hạn, false nếu kết thúc trước hôm nay
       //   fs.setOpen(fs.getEvaluationEndDate() != null && !fs.getEvaluationEndDate().isBefore(today));
       // fs.setOpen(dto.isOpen());
        // Lưu và trả về DTO
        FacultySemesterEntity saved = facultySemesterRepository.save(fs);
        return facultySemesterMapper.toDTO(saved);
    }


}

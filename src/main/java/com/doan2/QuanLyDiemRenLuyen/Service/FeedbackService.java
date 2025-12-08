package com.doan2.QuanLyDiemRenLuyen.Service;

import com.doan2.QuanLyDiemRenLuyen.DTO.FeedbackDTO;

import java.util.List;

public interface FeedbackService {
    public FeedbackDTO create(FeedbackDTO feedbackDTO);
    public List<FeedbackDTO> findAllByStudent(int studentId);
    public List<FeedbackDTO> findAll(int managerId);
    public FeedbackDTO update(FeedbackDTO feedbackDTO);
}

package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedbackDTO {
    private int feedbackId;
    private String content;
    private String image;
    private String gmail;
    private StudentDTO studentDTO;
    private ConductFormDTO conductFormDTO;
    private String responseContent;
    private boolean response;
    private ManagerDTO managerDTO;
    private LocalDateTime createAt;
    private LocalDateTime updatedDate;
}

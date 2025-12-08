package com.doan2.QuanLyDiemRenLuyen.Entity;

import com.doan2.QuanLyDiemRenLuyen.DTO.ConductFormDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="feedback")
@Getter
@Setter
public class FeedbackEntity {
    @Id
    @Column(name="feedback_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_id")
    @SequenceGenerator(name = "feedback_id", sequenceName = "feedback_id_seq", allocationSize = 1)
    private int feedbackId;
    @Column(name="content")
    private String content;
    @Column(name="image")
    private String image;
    @Column(name="gmail")
    private String gmail;
    @Column(name="createAt")
    private LocalDateTime createAt;
    @Column(name="updatedDate")
    private LocalDateTime updatedDate;
    @Column(name="response_content")
    private String responseContent;
    @Column(name="is_response")
    private boolean isResponse;
    @ManyToOne()
    @JoinColumn(name="studentId")
    private StudentEntity studentEntity;
    @ManyToOne()
    @JoinColumn(name="conductFormId")
    private ConductFormEntity conductFormEntity;
    @ManyToOne()
    @JoinColumn(name="managerId")
    private ManagerEntity managerEntity;
}

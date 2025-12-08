package com.doan2.QuanLyDiemRenLuyen.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name="conduct_form",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "semester_id"})
    }
)
public class ConductFormEntity {
    @Id
    @Column(name="conduct_form_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conduct_form_id")
    @SequenceGenerator(name = "conduct_form_id", sequenceName = "conduct_form_id_seq", allocationSize = 1)
    private int conductFormId;
    @Column(name="student_score")
    private int totalStudentScore;
    @Column(name="class_monitor_score")
    private int classMonitorScore;
    @Column(name="staff_score")
    private int staff_score;
    @Column(name="status")
    private String status;
    @Column(name="create_at")
    private LocalDateTime createAt;
    @Column(name="updated_date")
    private LocalDateTime updatedDate;
    @Column(name="manager_accept", nullable = true)
    private Integer managerAccept;
    @ManyToOne
    @JoinColumn(name="semester_id")
    private SemesterEntity semesterEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Student_id")
    @JsonBackReference
    private StudentEntity studentEntity;
    //cascade = CascadeType.ALL giúp khi lưu ConductForm sẽ lưu luôn ConductFormDetail
    @OneToMany(mappedBy ="conductFormEntity" ,cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    private List<ConductFormDetailEntity> conductFormDetailEntityList;
    @OneToMany(mappedBy ="conductFormEntity",cascade = CascadeType.ALL )
    private List<FeedbackEntity> feedbackEntities;
}

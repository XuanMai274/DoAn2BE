package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="semester")
public class SemesterEntity {
    @Id
    @Column(name="semester_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_id")
    @SequenceGenerator(name = "semester_id", sequenceName = "semester_id_seq", allocationSize = 1)
    private int semesterId;
    @Column(name="semester_name")
    private String semesterName;
    @Column(name="start_date")
    private LocalDateTime startDate;
    @Column(name="end_date")
    private  LocalDateTime endDate;
    @Column(name="year")
    private String year;
    @Column(name="is_open")
    private boolean isOpen;
    @Column(name = "evaluation_start_date")
    private LocalDate evaluationStartDate;
    @Column(name = "evaluation_end_date")
    private LocalDate evaluationEndDate;
    @OneToMany(mappedBy = "semesterEntity")
    private List<ConductFormEntity> conductFormEntityList;
    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<FacultySemesterEntity> facultySemesterList;


}

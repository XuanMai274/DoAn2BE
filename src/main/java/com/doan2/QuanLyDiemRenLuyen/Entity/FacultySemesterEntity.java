package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "faculty_semester")
public class FacultySemesterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_semester_id")
    @SequenceGenerator(name = "faculty_semester_id", sequenceName = "faculty_semester_id_seq", allocationSize = 1)
    private int id;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyEntity faculty;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(name = "evaluation_start_date")
    private LocalDate evaluationStartDate;

    @Column(name = "evaluation_end_date")
    private LocalDate evaluationEndDate;
}

package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.Class;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="Faculty")
public class FacultyEntity {
    @Id
    @Column(name="faculty_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_id")
    @SequenceGenerator(name = "faculty_id", sequenceName = "faculty_id_seq", allocationSize = 1)
    private int facultyId;
    @Column(name="faculty_name")
    private String facultyName;
    @OneToMany(mappedBy = "facultyId",cascade = CascadeType.ALL)
    private List<ClassEntity> classList;
    @OneToMany(mappedBy = "facultyEntity")
    private List<ManagerEntity> managerEntityList;
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<FacultySemesterEntity> facultySemesterList;
    @OneToMany(mappedBy = "facultyEntity",cascade = CascadeType.ALL)
    private List<NotificationEntity> notificationEntities;
}

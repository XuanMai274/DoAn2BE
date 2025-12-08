package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="class")
public class ClassEntity {
    @Id
    @Column(name="class_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_id")
    @SequenceGenerator(name = "class_id", sequenceName = "class_id_seq", allocationSize = 1)
    private int classId;
    @Column(name="class_name")
    private String className;
    @ManyToOne
    @JoinColumn(name="faculty_id")
    private FacultyEntity facultyId;
    @OneToMany(mappedBy = "classId")
    private List<StudentEntity> studentEntityList;
}

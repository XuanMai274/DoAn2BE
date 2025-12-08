package com.doan2.QuanLyDiemRenLuyen.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="student")
public class StudentEntity {
    @Id
    @Column(name="student_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id")
    @SequenceGenerator(name = "student_id", sequenceName = "student_id_seq", allocationSize = 1)
    private int studentId;
    @Column(name="fullname")
    private String fullname;
    @Column(name="gender")
    private String Gender;
    @Column(name="email")
    private String email;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="status")
    private String status;
    @Column(name="is_class_monitor")
    private Boolean isClassMonitor;
    @ManyToOne
    @JoinColumn(name="class_id")
    private ClassEntity classId;
    @OneToOne
    @JoinColumn(name="account_id")
    @JsonBackReference
    private AccountEntity accountEntity;
    @OneToMany(mappedBy = "studentEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ConductFormEntity> conductFormEntities;
    @OneToMany(mappedBy = "studentEntity" ,cascade = CascadeType.ALL)
    private List<FeedbackEntity> feedbackEntities;

}

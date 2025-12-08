package com.doan2.QuanLyDiemRenLuyen.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="manager")
public class ManagerEntity {
    @Id
    @Column(name="manager_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_id")
    @SequenceGenerator(name = "manager_id", sequenceName = "manager_id_seq", allocationSize = 1)
    private int managerId;
    @Column(name="fullname")
    private String fullname;
    @Column(name="email")
    private String email;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="status")
    private String status;
    @OneToMany(mappedBy = "managerEntity")
    private List<NotificationEntity> notificationEntity;
    @ManyToOne
    @JoinColumn(name="faculty_id")
    private FacultyEntity facultyEntity;
    @OneToOne
    @JoinColumn(name="account_id")
    @JsonBackReference
    private AccountEntity accountEntity;
    @OneToMany(mappedBy = "managerEntity")
    private List<FeedbackEntity> feedbackEntities;

}

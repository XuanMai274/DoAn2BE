package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="notification")
public class NotificationEntity {
    @Id
    @Column(name="notification_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id")
    @SequenceGenerator(name = "notification_id", sequenceName = "notification_id_seq", allocationSize = 1)
    private int notificationId;
    @Column(name="title")
    private String title;
    @Column(name="content")
    private String content;
    @Column(name="status")
    private String status;
    @Column(name="type")
    private String type;
    @CreationTimestamp
    @Column(name="create_at")
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(name="update_at")
    private LocalDateTime updateAt;
    @Column(name="startDate")
    private LocalDateTime startDate;
    @Column (name="endDate")
    private LocalDateTime endDate;
    @ManyToOne
    @JoinColumn(name="manager_id")
    private ManagerEntity managerEntity;
    @ManyToOne
    @JoinColumn(name="faculty_id")
    private FacultyEntity facultyEntity;
}

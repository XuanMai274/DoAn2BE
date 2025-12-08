package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification_read")
public class NotificationReadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_read_id")
    @SequenceGenerator(name = "notification_read_id", sequenceName = "notification_read_id_seq", allocationSize = 1)
    private int id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private NotificationEntity notification;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}


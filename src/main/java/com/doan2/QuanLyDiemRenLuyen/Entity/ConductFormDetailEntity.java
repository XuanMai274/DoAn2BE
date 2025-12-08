package com.doan2.QuanLyDiemRenLuyen.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="conduct_form_detail")
public class ConductFormDetailEntity {
    @Id
    @Column(name="conduct_form_detail_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conduct_form_detail_id")
    @SequenceGenerator(name = "conduct_form_detail_id", sequenceName = "conduct_form_detail_id_seq", allocationSize = 1)
    private int conductFormDetailId;
    @Column(name="self_score")
    private int selfScore;
    @Column(name="class_monitor_score")
    private int classMonitorScore;
    @Column(name="staff_score")
    private int staff_score;
    @Column(name="comment")
    private String comment;
    @Column(name="file")
    private String file;
    @Column(name="public_id")
    private String public_id;
    @ManyToOne
    @JoinColumn(name="conduct_form_id")
    @JsonBackReference
    private ConductFormEntity conductFormEntity;
    @ManyToOne
    @JoinColumn(name="criteria_id")
    private CriteriaEntity criteriaEntity;


}

package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="criteria")
public class CriteriaEntity {
    @Id
    @Column(name="criteria_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "criteria_id")
    @SequenceGenerator(name = "criteria_id", sequenceName = "criteria_id_seq", allocationSize = 1)
    private int criteriaId;
    @Column(name="criteria_name")
    private String criteriaName;
    @Column(name="criteria_detail")
    private String criteria_detail;
    @Column(name="max_score")
    private int maxScore;
    @ManyToOne
    @JoinColumn(name="criteria_type_id")
    private CriteriaTypeEntity criteriaTypeEntity;
    @OneToMany(mappedBy = "criteriaEntity")
    private List<ConductFormDetailEntity> conductFormDetailEntityList;
}

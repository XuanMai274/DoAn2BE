package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="criteria_type")
public class CriteriaTypeEntity {
    @Id
    @Column(name="criteria_type_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "criteria_type_id")
    @SequenceGenerator(name = "criteria_type_id", sequenceName = "criteria_type_id_seq", allocationSize = 1)
    private int criteriaTypeId;
    @Column(name="criteriaType_name")
    private String criteriaTypeName;
    @Column(name="max_score")
    private int maxScore;
    @Column(name="is_active")
    private boolean isActive;
    @OneToMany(mappedBy = "criteriaTypeEntity")
    private List<CriteriaEntity> criteriaEntityList;

}

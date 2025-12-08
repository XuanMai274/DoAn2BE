package com.doan2.QuanLyDiemRenLuyen.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="role")
public class RoleEntity {
    @Id
    @Column(name="role_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id")
    @SequenceGenerator(name = "role_id", sequenceName = "role_id_seq", allocationSize = 1)
    private int roleId;
    @Column(name="role_name")
    private String roleName;
    @OneToMany (mappedBy = "role",cascade = CascadeType.ALL)
    private List<AccountEntity> accountEntityList;
}

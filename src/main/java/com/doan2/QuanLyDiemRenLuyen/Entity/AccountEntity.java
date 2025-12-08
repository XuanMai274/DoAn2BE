package com.doan2.QuanLyDiemRenLuyen.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="account")
public class AccountEntity {
    @Id
    @Column(name="account_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id")
    @SequenceGenerator(name = "account_id", sequenceName = "account_id_seq", allocationSize = 1)
    private int accountId;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="enable")
    private Boolean enable;
    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleEntity role;
    @OneToOne(mappedBy = "accountEntity")
    @JsonManagedReference
    private StudentEntity studentEntity;
    @OneToOne(mappedBy = "accountEntity")
    @JsonManagedReference
    private ManagerEntity managerEntity;

}

package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerUpdateDTO {
    private int managerId;
    private String fullname;
    private String email;
    private String phoneNumber;
}

package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDTO {
    private int studentId;
    private String fullname;
    private String gender;
    private String email;
    private String phoneNumber;
    private String status;
    private Boolean isClassMonitor;

    private ClassDTO classDTO;
    private AccountDTO accountDTO;
    private List<ConductFormDTO> conductFormDTO;
}

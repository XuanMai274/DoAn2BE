package com.doan2.QuanLyDiemRenLuyen.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;
    private String token;
}

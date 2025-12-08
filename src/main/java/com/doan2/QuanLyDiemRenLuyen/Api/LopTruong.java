package com.doan2.QuanLyDiemRenLuyen.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LopTruong {
    @GetMapping("/manager")
    String test(){
        return "Truy cap dc vao /manager";
    }
}

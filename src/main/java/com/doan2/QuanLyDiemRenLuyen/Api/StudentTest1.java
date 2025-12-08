package com.doan2.QuanLyDiemRenLuyen.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentTest1 {
    @GetMapping("/student")
    String test(){
        return "Truy cap dc vao studnet";
    }
}

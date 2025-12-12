package com.doan2.QuanLyDiemRenLuyen.Api;

import com.doan2.QuanLyDiemRenLuyen.DTO.AuthenticationRequest;
import com.doan2.QuanLyDiemRenLuyen.DTO.AuthenticationResponse;
import com.doan2.QuanLyDiemRenLuyen.DTO.ManagerDTO;
import com.doan2.QuanLyDiemRenLuyen.DTO.StudentDTO;
import com.doan2.QuanLyDiemRenLuyen.Service.ManagerService;
import com.doan2.QuanLyDiemRenLuyen.Service.StudentService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomJWT;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetailService;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthenticateAPI {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    CustomeUserDetailService customeUserDetailService;
    @Autowired
    CustomJWT jwt;
    @Autowired
    StudentService studentService;
    @Autowired
    ManagerService managerService;
    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authentication) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authentication.getUsername(), authentication.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc mật khẩu không đúng");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không tồn tại hoặc đã bị vô hiệu hóa");
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi trong quá trình đăng nhập");
        }

        System.out.println("Authentication successful for user: " + authentication.getUsername());
        UserDetails user = customeUserDetailService.loadUserByUsername(authentication.getUsername());
        // ép kiểu
        CustomeUserDetails customUser = (CustomeUserDetails) user;
        // kiem tra xem nguoi dung hien tai dang role nào
        if(customUser.getAccountEntity().getRole().getRoleName().equals("MANAGER")){
         //   StudentDTO studentDTO=studentService.findByUsername(user.getUsername());
            ManagerDTO managerDTO= managerService.findByAccountId(((CustomeUserDetails) user).getAccountEntity().getAccountId());
            AuthenticationResponse authenticationResponse = jwt.generateToken(user.getUsername(),managerDTO.getManagerId(), "MANAGER");
            authenticationResponse.setRole(customUser.getAccountEntity().getRole().getRoleName());
            return ResponseEntity.ok(authenticationResponse);
        }
        else if(customUser.getAccountEntity().getRole().getRoleName().equals("STUDENT"))
        {
            StudentDTO studentDTO=studentService.findByUsername(user.getUsername());
            AuthenticationResponse authenticationResponse = jwt.generateToken(user.getUsername(), studentDTO.getStudentId(), "STUDENT");
            if(studentDTO.getIsClassMonitor()){
                authenticationResponse.setRole("CLASS_MONITOR");
            }
            else{
                authenticationResponse.setRole("STUDENT");
            }
            return ResponseEntity.ok(authenticationResponse);
        }
        else if(customUser.getAccountEntity().getRole().getRoleName().equals("MANAGER_1")){
            ManagerDTO managerDTO= managerService.findByAccountId(((CustomeUserDetails) user).getAccountEntity().getAccountId());
            AuthenticationResponse authenticationResponse = jwt.generateToken(user.getUsername(),managerDTO.getManagerId() , "MANAGER_1");
            authenticationResponse.setRole(customUser.getAccountEntity().getRole().getRoleName());
            return ResponseEntity.ok(authenticationResponse);
        }
        return null;
    }
    // refresh token
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) throws Exception {
        // lấy refresh token từ header
        String refreshToken=request.getHeader("Authorization-Refresh");
        // kiểm tra refresh token có hợp lệ không
        if(jwt.validateToken(refreshToken)==200){
            AuthenticationResponse authenticationResponse=new AuthenticationResponse();
            authenticationResponse.setAccessToken(jwt.refreshToken(jwt.extractUserName(refreshToken),Integer.parseInt(jwt.extractUserId(refreshToken)),"admin"));
            authenticationResponse.setRefreshToken(refreshToken);
            return ResponseEntity.ok(authenticationResponse);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Trả về 500 nếu có lỗi hệ thống
        }
    }
    private String determineRole(List<GrantedAuthority> authorities) {
        if (authorities.contains(new SimpleGrantedAuthority("CREATE")) &&
                authorities.contains(new SimpleGrantedAuthority("UPDATE")) &&
                authorities.contains(new SimpleGrantedAuthority("DELETE"))) {
            return "MANAGER";
        } else if (authorities.contains(new SimpleGrantedAuthority("CREATE")) &&
                authorities.contains(new SimpleGrantedAuthority("UPDATE"))) {
            return "STUDENT";
        }
        return "USER";
    }
}

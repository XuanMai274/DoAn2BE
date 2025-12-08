package com.doan2.QuanLyDiemRenLuyen.Utill;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired CustomJWT jwt;
    @Autowired
    CustomeUserDetailService customeUserDetailService;

    // cấu hình các url được bỏ qua không cần filter
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        boolean skip = path.equals("/authenticate") || path.equals("/register")
                || path.equals("/refreshToken") || path.startsWith("/public/")
                || path.startsWith("/uploads/");
        System.out.println("shouldNotFilter path=" + path + " => " + skip);
        return skip;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // lấy token và refresh token từ header
        String authorizationHeader=request.getHeader("Authorization");
        String username=null;
        String token=null;
        String refreshToken=request.getHeader("Authorization-Refresh");
        // kiểm tra token xem token có chưa bear token hay không
        if(authorizationHeader!=null &&authorizationHeader.startsWith("Bearer ")){
            token=authorizationHeader.substring(7);
            username=jwt.extractUserName(token);
        }
        // nếu như token không có trong header thì lấy token từ Cookie
        if(token==null){
            token=getCookieValue(request, "authToken");
            //nếu token có trong Cookie thì lấy user name
            if(token!=null){
                username=jwt.extractUserName(token);
            }
        }
        // bắt đầu kiểm tra token
        if(token!=null){
            try {
                // kiểm tra token có đúng nhưng hết hạn hay không=> nếu có thì đi vào if
                int tokenStatus=0;
                tokenStatus=jwt.validateToken(token);
                if(tokenStatus==401){
                    // kiểm tra refresh token
                    if(jwt.validateToken(refreshToken)==200){
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    else{
                        // yêu cầu đăng nhập lại
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }
                if(tokenStatus==400){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                // token hợp lệ
                if(username!=null&&jwt.validateToken(token)==200&& SecurityContextHolder.getContext().getAuthentication() == null){
                    try {
                        UserDetails userDetails = customeUserDetailService.loadUserByUsername(username);
                        CustomeUserDetails customUserDetail = (CustomeUserDetails) userDetails;
                        UsernamePasswordAuthenticationToken authToken
                                =new UsernamePasswordAuthenticationToken(customUserDetail,null,userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                        UserDetails a = (UserDetails) authentication.getPrincipal();
                        CustomeUserDetails b = (CustomeUserDetails) a;
                        Authentication authenticationn= SecurityContextHolder.getContext().getAuthentication();
                        UserDetails user=(UserDetails) authentication.getPrincipal();
                        // ép user về customUserDetail
                        CustomeUserDetails custome=(CustomeUserDetails) user;
                       // System.out.println("thông tin của User: "+ custome.getAccountEntity().getIdUser());
                    } catch (UsernameNotFoundException e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"User not found\"}");
                        return;
                    }

                }
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                System.out.println("Lỗi tại jwt request fillter: "+e.getMessage());
                throw new RuntimeException(e);
            }
        }else if(token==null){
            // nếu token rỗng
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing");
            return;
        }

    }
    // hàm lấy token từ cookie
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

package com.doan2.QuanLyDiemRenLuyen.Config;

import com.doan2.QuanLyDiemRenLuyen.Utill.CustomAuthenticationEntryPoint;
import com.doan2.QuanLyDiemRenLuyen.Utill.CustomeUserDetailService;
import com.doan2.QuanLyDiemRenLuyen.Utill.JwtRequestFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    CustomeUserDetailService customeUserDetailService;
    @Autowired
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    JwtRequestFilter jwtRequestFilter;
    //SecurityContext sẽ được kế thừa từ Thread cha sang các Thread con, để có thể lấy được id
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return customeUserDetailService;
    }
    //hàm này sẽ được sử dụng để mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    // cấu hình thêm phần này để nếu sai mật khẩu hoặc người dùng không hợp lệ thì sẽ ném ra lỗi thật sự và dừng lại, và không cố gắng retry  nhiều lần dẫn đến lỗi vô tận
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService()); // sử dụng service của bạn
        provider.setPasswordEncoder(passwordEncoder());      // sử dụng password encoder của bạn
        provider.setHideUserNotFoundExceptions(false);       // tắt hideUserNotFoundExceptions
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.authenticationProvider(daoAuthenticationProvider());

        return authBuilder.build();
    }

    // cấu hình bỏ qua những tài nguyên tĩnh
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**","/js/**","/vendors/**","/assets/**","/admin/**","/profile/**","/uploads/**","/image/**","/chatcss/**","/post/**","/ws/**"); // Bỏ qua các yêu cầu đến các tài nguyên tĩnh
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Nếu ảnh được truy cập bằng: http://localhost:8080/uploads/image.jpg
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Tắt CSRF
                . cors(cors -> cors.configurationSource(corsConfigurationSource())) //Bật CORS
                .  authorizeHttpRequests( authorize -> authorize
                        .requestMatchers("/authenticate","/public/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/manager/**").hasAuthority("MANAGER")
                        .requestMatchers("/manager1/**").hasAuthority("MANAGER_1")
                        .requestMatchers("/ClassMonitor").hasAuthority("CLASS_MONITOR")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Đảm bảo không sử dụng session server-side
                );
        ;
        return http.build();

    }

}

package com.example.bookreading.config; // Đảm bảo đúng package của bạn

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Yêu cầu Spring Security sử dụng bean CorsConfigurationSource ở dưới
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Tắt CSRF
            .csrf(csrf -> csrf.disable())
            
            // 3. Cho phép tất cả các request
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            )
            
            // 4. Tắt các kiểu đăng nhập mặc định
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // 5. ĐÂY LÀ PHẦN CẤU HÌNH CORS MỚI
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 6. Đặt các URL frontend được phép
        configuration.setAllowedOrigins(Arrays.asList(
                "http://127.0.0.1:5500",
                "https://thebooks-java-pikqimfah-phong11084-projects.vercel.app"
        ));
        
        // 7. Đặt các phương thức (GET, POST...) được phép
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 8. Cho phép tất cả các header
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // 9. Cho phép gửi cookie
        configuration.setAllowCredentials(true);
        
        // 10. Áp dụng cấu hình này cho tất cả các đường dẫn
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
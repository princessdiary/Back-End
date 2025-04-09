package edu.sugang.config;

import edu.sugang.security.JwtAuthenticationFilter;
import edu.sugang.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity // Spring Security의 웹 보안 지원을 활성화
@RequiredArgsConstructor
@Slf4j // 로깅 위한 Log 객체 자동 생성
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpBasic -> httpBasic.disable())  // REST API이므로 basic auth 사용하지 않음
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .cors(Customizer.withDefaults())  // CORS 설정 추가
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 사용하지 않음
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/admin/login").permitAll()  // 모든 사용자 접근 허용
                                .requestMatchers("/schedule", "/enrollment/**", "/lecture", "/basket/**", "/notice/**", "/faq/**", "/api/**", "/webflux/**", "/accessibilityfeature/**", "/mypage/**", "/enrollment").hasAuthority("USER") // user 권한이 있어야 접근 가능
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")  // admin 권한이 있어야 접근 가능
                                .requestMatchers("/logout").authenticated()
                                .anyRequest().authenticated()  // 모든 요청에 대해 인증 필요
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")  // 로그아웃 URL
                                .logoutSuccessHandler(logoutSuccessHandler())  // 사용자 로그아웃 성공 핸들러
                                .permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);  // JWT 필터 추가

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, IOException {
                // 로그아웃 성공 시 200 OK
                response.setStatus(HttpServletResponse.SC_OK);

                // 로그인 화면으로 리다이렉트
                response.sendRedirect("/login");
            }
        };
    }
}
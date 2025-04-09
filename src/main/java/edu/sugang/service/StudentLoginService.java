package edu.sugang.service;

import edu.sugang.domain.Student;
import edu.sugang.dto.JwtTokenDTO;
import edu.sugang.dto.response.StudentDTO;
import edu.sugang.repository.StudentRepository;
import edu.sugang.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudentLoginService {
    private final StudentRepository studentRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public StudentDTO signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 학생에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenDTO jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. 인증된 사용자 정보로부터 studentNumber와 studentId를 조회
        Student student = studentRepository.findByStudentNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        return new StudentDTO(
                student.getStudentNumber(),
                student.getId(),
                student.getDepartment().getDepartmentName(),
                student.getStudentName(),
                student.getGrade(),
                student.getMaxCredits(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken());
    }
}

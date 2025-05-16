package com.enp.service;

import com.enp.domain.dto.request.AuthRequest;
import com.enp.domain.dto.request.MyPageEditRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.response.*;
import com.enp.domain.entity.User;
import com.enp.repository.UserRepository;
import com.enp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public SignupResponseDTO signupService(SignupRequestDTO signupRequestDto){
        if (userRepository.existsByLoginId(signupRequestDto.getLoginId())) {
            return SignupResponseDTO.builder()
                    .userId(null)
                    .isDuplicated(true)
                    .build();
        }
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        // 사용자 엔티티 생성 (빌더 패턴 사용)
        User newUser = User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword) // 암호화된 비밀번호 저장
                .nickname(signupRequestDto.getNickname())
                // 기본값은 User 엔티티에서 설정하거나 여기서 명시적으로 설정 가능
                .todayQuizCount(3) // 예시 기본값
                .point(0L)            // 예시 기본값
                .build();

        // 사용자 정보 저장
        User savedUser = userRepository.save(newUser);

        return SignupResponseDTO.builder()
                .userId(savedUser.getId())
                .nickname(savedUser.getNickname())
                .loginId(savedUser.getLoginId())
                .isDuplicated(false)
                .build();
    }

    public AuthResponse loginService(AuthRequest authRequest){
        User user = userRepository.findByLoginId(authRequest.getLoginId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 3. 응답 DTO 만들기
        String jwt = jwtUtil.generateToken(authRequest.getLoginId());
        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setUserId(user.getId());
        response.setIsLogin(true);
        response.setNickname(user.getNickname());
        response.setLoginId(user.getLoginId());

        return response;
    }
    public MyPageResponseDTO myPageService(Long userId){
        return userRepository.findById(userId)
                .map(user-> MyPageResponseDTO.builder()
                .nickname(user.getNickname())
                .build())

                .orElseThrow(()->new RuntimeException("마이페이지 조회 실패:userID가"+userId+"인 사용자를 찾을 수 없습니다"));
    }
    public MyPageEditResponseDTO myPageEditService(Long userId, MyPageEditRequestDTO myPageEditRequestDTO){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("마이페이지 수정 실패: ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
        if (myPageEditRequestDTO.getNickname() != null && !myPageEditRequestDTO.getNickname().equals(user.getNickname())) {
            user.setNickname(myPageEditRequestDTO.getNickname());
        }
        if(myPageEditRequestDTO.getPassword()!=null&&!myPageEditRequestDTO.getPassword().equals(user.getPassword())){
            user.setPassword(myPageEditRequestDTO.getPassword());
        }
        userRepository.save(user);
        return MyPageEditResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .loginId(user.getLoginId())
                .build();
    }
    public MyPageEditCheckResponseDTO myPageEditCheckService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("마이페이지 수정 조회 실패: ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
        return MyPageEditCheckResponseDTO.builder()
                .nickname(user.getNickname())
                .loginId(user.getLoginId())
                .build();
    }

}

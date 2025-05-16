package com.enp.service;

import com.enp.domain.dto.request.AuthRequest;
import com.enp.domain.dto.request.MyPageEditRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.request.TextSizeOrLineGapEditRequestDTO;
import com.enp.domain.dto.response.*;
import com.enp.domain.entity.User;
import com.enp.repository.UserRepository;
import com.enp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public SignupResponseDTO signupService(SignupRequestDTO signupRequestDto){
        boolean isExist=userRepository.existsByLoginId(signupRequestDto.getLoginId());
        if(!isExist){
            User user=User.builder()
                    .nickname(signupRequestDto.getNickname())
                    .loginId(signupRequestDto.getLoginId())
                    .password(signupRequestDto.getPassword())
                    .todayQuizCount(3)
                    .point(0L)
                    .textSize(1)
                    .lineGap(1)
                    .build();
            User savedUser = userRepository.save(user);
            return SignupResponseDTO.builder()
                    .userId(savedUser.getId())
                    .nickname(savedUser.getNickname())
                    .loginId(savedUser.getLoginId())
                    .isDuplicated(false)
                    .build();
        }
        return  SignupResponseDTO.builder()
                .userId(null)
                .isDuplicated(true)
                .build();
    }

    public AuthResponse loginService(AuthRequest authRequest){
        User user = userRepository.findByLoginId(authRequest.getLoginId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        if(!Objects.equals(authRequest.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .isLogin(false)
                    .build();
        }
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
                .textSize(user.getTextSize())
                .lineGap(user.getLineGap())
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
                .textSize(user.getTextSize())
                .build();
    }

    public TextSizeOrLineGapCheckResponseDTO textSizeOrLineGapCheckService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("글자크기,글간격 조회 실패: ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
        return TextSizeOrLineGapCheckResponseDTO.builder()
                .userId(user.getId())
                .textSize(user.getTextSize())
                .lineGap(user.getLineGap())
                .build();
    }
    public TextSizeOrLineGapEditResponseDTO textSizeOrLineGapEditService(Long userId, TextSizeOrLineGapEditRequestDTO textSizeOrLineGapEditRequestDTO){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(" 글자크기,글간격 수정 실패: ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
        if(!Objects.equals(user.getTextSize(), textSizeOrLineGapEditRequestDTO.getTextSize())){
            user.setTextSize(textSizeOrLineGapEditRequestDTO.getTextSize());
        }
        if(!Objects.equals(user.getLineGap(),textSizeOrLineGapEditRequestDTO.getLineGap())){
            user.setLineGap(textSizeOrLineGapEditRequestDTO.getLineGap());
        }
        userRepository.save(user);
        return TextSizeOrLineGapEditResponseDTO.builder()
                .userId(user.getId())
                .build();
    }
}

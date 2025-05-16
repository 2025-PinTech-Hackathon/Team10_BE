package com.enp.service;

import com.enp.domain.dto.request.LoginRequestDTO;
import com.enp.domain.dto.request.MyPageEditRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.request.TextSizeOrLineGapEditRequestDTO;
import com.enp.domain.dto.response.*;
import com.enp.domain.entity.User;
import com.enp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    public UserRepository userRepository;
    public SignupResponseDTO signupService(SignupRequestDTO signupRequestDto){
        boolean isExist=userRepository.existsByLoginId(signupRequestDto.getLoginId());
        if(!isExist){
            User user=User.builder()
                    .nickname(signupRequestDto.getNickname())
                    .loginId(signupRequestDto.getLoginId())
                    .password(signupRequestDto.getPassword())
                    .todayQuizCount(3)
                    .point(0L)
                    .readCount(0L)
                    .textSize(1)
                    .lineGap(1)
                    .build();
            userRepository.save(user);
            return SignupResponseDTO.builder()
                    .userId(user.getId())
                    .isDuplicated(false)
                    .build();
        }
        return  SignupResponseDTO.builder()
                .userId(null)
                .isDuplicated(true)
                .build();
    }

    public LoginResponseDTO loginService(LoginRequestDTO loginRequestDTO){
        boolean isExist=userRepository.existsByLoginId(loginRequestDTO.getLoginId());
        if(isExist){
            String password = loginRequestDTO.getPassword();
            User user=userRepository.findByLoginId(loginRequestDTO.getLoginId()).
                    orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. 로그인 ID: " + loginRequestDTO.getLoginId())); // 사용자 정의 예외
            if(user.getPassword().equals(password)){
                return LoginResponseDTO.builder()
                        .userId(user.getId())
                        .isLogin(true)
                        .build();
            }
        }
        return LoginResponseDTO.builder()
                .userId(null)
                .isLogin(false)
                .build();
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
                .password(user.getPassword())
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

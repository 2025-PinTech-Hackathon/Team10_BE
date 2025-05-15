package com.enp.service;

import com.enp.domain.dto.request.LoginRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.response.LoginResponseDTO;
import com.enp.domain.dto.response.MyPageResponseDTO;
import com.enp.domain.dto.response.SignupResponseDTO;
import com.enp.domain.entity.User;
import com.enp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            Optional<User> user=userRepository.findByLoginId(loginRequestDTO.getLoginId());
            if(user.get().getPassword().equals(password)){
                return LoginResponseDTO.builder()
                        .userId(user.get().getId())
                        .isLogin(true)
                        .build();
            }
        }
        return LoginResponseDTO.builder()
                .userId(null)
                .isLogin(false)
                .build();
    }
    public MyPageResponseDTO myPageService(Long userid){
        Optional<User> user=userRepository.findById(userid);
        return MyPageResponseDTO.builder()
                .nickname(user.get().getNickname())
                .textSize(user.get().getTextSize())
                .lineGap(user.get().getLineGap())
                .build();
    }
}

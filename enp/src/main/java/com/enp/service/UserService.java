package com.enp.service;

import com.enp.domain.dto.request.SignupRequestDto;
import com.enp.domain.dto.response.SignupResponseDTO;
import com.enp.domain.entity.User;
import com.enp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserRepository userRepository;
    public SignupResponseDTO signupService(SignupRequestDto signupRequestDto){
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
}

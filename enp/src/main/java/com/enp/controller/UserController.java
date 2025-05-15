package com.enp.controller;

import com.enp.domain.dto.request.LoginRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.response.LoginResponseDTO;
import com.enp.domain.dto.response.SignupResponseDTO;
import com.enp.service.UserService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    public UserService userService;
    @PostMapping("/singup")
    public ApiResponse<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDto){

        SignupResponseDTO signupResponseDTO=userService.signupService(signupRequestDto);

        return ApiResponse.onSuccess(signupResponseDTO);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO=userService.loginService(loginRequestDTO);
        return ApiResponse.onSuccess(loginResponseDTO);
    }
}

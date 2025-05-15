package com.enp.controller;

import com.enp.domain.dto.request.LoginRequestDTO;
import com.enp.domain.dto.request.MyPageEditRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.response.*;
import com.enp.service.UserService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}")
    public ApiResponse<MyPageResponseDTO> myPage(@PathVariable Long userId){
        MyPageResponseDTO myPageResponseDTO=userService.myPageService(userId);
        return ApiResponse.onSuccess(myPageResponseDTO);
    }
    @PatchMapping("/{userId}/edit")
    public ApiResponse<MyPageEditResponseDTO> myPageEdit(@PathVariable Long userId ,@RequestBody MyPageEditRequestDTO myPageEditRequestDTO){
        MyPageEditResponseDTO myPageEditResponseDTO=userService.myPageEditService(userId,myPageEditRequestDTO);
        return ApiResponse.onSuccess(myPageEditResponseDTO);
    }
    @GetMapping("/{userId}/mypage")
    public ApiResponse<MyPageEditCheckResponseDTO> myPageEditCheck(@PathVariable Long userId){
        MyPageEditCheckResponseDTO myPageEditCheckResponseDTO=userService.myPageEditCheckService(userId);
        return ApiResponse.onSuccess(myPageEditCheckResponseDTO);
    }
}

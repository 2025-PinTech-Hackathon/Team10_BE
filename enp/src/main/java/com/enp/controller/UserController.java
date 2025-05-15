package com.enp.controller;

import com.enp.domain.dto.request.SignupRequestDto;
import com.enp.domain.dto.response.SignupResponseDTO;
import com.enp.service.UserService;
import com.enp.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ApiResponse<SignupResponseDTO> signup(@RequestBody SignupRequestDto signupRequestDto){

        SignupResponseDTO signupResponseDTO=userService.signupService(signupRequestDto);

        return ApiResponse.onSuccess(signupResponseDTO);
    }

}

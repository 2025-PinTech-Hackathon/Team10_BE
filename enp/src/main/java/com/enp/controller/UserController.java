package com.enp.controller;

import com.enp.domain.dto.request.AuthRequest;
import com.enp.domain.dto.request.MyPageEditRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.request.itemRequestDTO;
import com.enp.domain.dto.response.*;
import com.enp.domain.entity.Item;
import com.enp.service.UserService;
import com.enp.util.ApiResponse;
import com.enp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDto){
        SignupResponseDTO signupResponseDTO=userService.signupService(signupRequestDto);
        return ApiResponse.onSuccess(signupResponseDTO);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword())
        );
        AuthResponse authResponse = userService.loginService(request);
        return ApiResponse.onSuccess(authResponse);
    }

    @GetMapping("")
    public ApiResponse<MyPageResponseDTO> myPage(@AuthenticationPrincipal String loginId){
        MyPageResponseDTO myPageResponseDTO=userService.myPageService(loginId);
        return ApiResponse.onSuccess(myPageResponseDTO);
    }
    @PatchMapping("/edit")
    public ApiResponse<MyPageEditResponseDTO> myPageEdit(@AuthenticationPrincipal String loginId, @RequestBody MyPageEditRequestDTO myPageEditRequestDTO){
        MyPageEditResponseDTO myPageEditResponseDTO=userService.myPageEditService(loginId,myPageEditRequestDTO);
        return ApiResponse.onSuccess(myPageEditResponseDTO);
    }
    @GetMapping("/mypage")
    public ApiResponse<MyPageEditCheckResponseDTO> myPageEditCheck(@AuthenticationPrincipal String loginId){
        MyPageEditCheckResponseDTO myPageEditCheckResponseDTO=userService.myPageEditCheckService(loginId);
        return ApiResponse.onSuccess(myPageEditCheckResponseDTO);
    }
    @PostMapping("/item")
    public ApiResponse<ItemResponseDTO> purchase(@AuthenticationPrincipal String loginId, @RequestBody itemRequestDTO itemId){
        ItemResponseDTO  itemResponseDTO = userService.purchaseItem(itemId, loginId);
        return ApiResponse.onSuccess(itemResponseDTO);
    }

}

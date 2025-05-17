package com.enp.service;

import com.enp.domain.dto.request.AuthRequest;
import com.enp.domain.dto.request.MyPageEditRequestDTO;
import com.enp.domain.dto.request.SignupRequestDTO;
import com.enp.domain.dto.response.*;
import com.enp.domain.entity.Item;
import com.enp.domain.entity.User;
import com.enp.repository.ItemRepository;
import com.enp.repository.UserRepository;
import com.enp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
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
    public MyPageResponseDTO myPageService(String loginId){
        return userRepository.findByLoginId(loginId)
                .map(user-> MyPageResponseDTO.builder()
                .nickname(user.getNickname())
                .point(user.getPoint())
                .build())

                .orElseThrow(()->new RuntimeException("마이페이지 조회 실패:userID가"+loginId +"인 사용자를 찾을 수 없습니다"));
    }
    public MyPageEditResponseDTO myPageEditService(String loginId, MyPageEditRequestDTO myPageEditRequestDTO){
        // 2. DB에서 사용자 정보 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("마이페이지 수정 실패: ID가 " + loginId + "인 사용자를 찾을 수 없습니다."));

        // 3. 닉네임 변경 처리
        // DTO에서 받은 닉네임이 null이 아니고, 비어있지 않으며, 기존 닉네임과 다를 경우에만 업데이트
        if (StringUtils.hasText(myPageEditRequestDTO.getNickname()) &&
                !myPageEditRequestDTO.getNickname().equals(user.getNickname())) {
            user.setNickname(myPageEditRequestDTO.getNickname());
        }

        // 4. 비밀번호 변경 처리
        // DTO에서 받은 비밀번호가 null이 아니고, 비어있지 않을 경우 (즉, 사용자가 비밀번호 변경을 원할 경우)
        if (StringUtils.hasText(myPageEditRequestDTO.getPassword())) {
            // 새로운 비밀번호를 암호화하여 설정
            user.setPassword(passwordEncoder.encode(myPageEditRequestDTO.getPassword()));
        }

        // 5. 변경된 사용자 정보 저장 (JPA의 더티 체킹에 의해 @Transactional 범위 내에서는 자동 저장될 수 있으나, 명시적으로 save 호출도 가능)
        userRepository.save(user); // 명시적으로 호출할 필요는 없을 수 있음

        // 6. 응답 DTO 생성 및 반환
        //    주의: 응답 DTO에 암호화된 비밀번호를 포함하지 않도록 합니다.
        return MyPageEditResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }
    public MyPageEditCheckResponseDTO myPageEditCheckService(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("마이페이지 수정 조회 실패: ID가 " + loginId + "인 사용자를 찾을 수 없습니다."));
        return MyPageEditCheckResponseDTO.builder()
                .nickname(user.getNickname())
                .loginId(user.getLoginId())
                .build();
    }

    public ItemResponseDTO purchaseItem(Long itemId, String loginId) {
        Optional<Item> item = itemRepository.findById(itemId);
        Optional<User> user = userRepository.findByLoginId(loginId);
        Long userPoint = user.get().getPoint();
        Long itemPrice = item.get().getPrice();
        if(userPoint>=itemPrice){
            user.get().setPoint(userPoint-itemPrice);
            userRepository.save(user.get());
            return ItemResponseDTO.builder().isPurchase(true).reservedPoint(userPoint-itemPrice).build();
        }
        return ItemResponseDTO.builder().isPurchase(false).reservedPoint(userPoint).build();
    }
}

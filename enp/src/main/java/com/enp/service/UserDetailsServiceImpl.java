package com.enp.service; // 실제 프로젝트 패키지 구조에 맞게 변경하세요.

import com.enp.domain.entity.User; // 프로젝트의 User 엔티티 (가정)
import com.enp.repository.UserRepository; // 프로젝트의 UserRepository (가정)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // 실제로는 사용자의 권한(Role)을 여기에 매핑해야 합니다.

@Service // 이 어노테이션을 통해 Spring 컨테이너에 빈으로 등록됩니다.
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository; // UserRepository를 주입받습니다.

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // 데이터베이스에서 loginId를 기준으로 사용자 정보를 조회합니다.
        // 실제 프로젝트의 User 엔티티와 UserRepository를 사용해야 합니다.
        User user = userRepository.findByLoginId(loginId) // UserRepository에 findByLoginId 메소드가 있다고 가정
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. Login ID: " + loginId));

        // Spring Security가 사용하는 UserDetails 객체로 변환하여 반환합니다.
        // user.getPassword()는 데이터베이스에 저장된 암호화된 비밀번호여야 합니다.
        // 마지막 인자는 사용자의 권한(authorities) 목록입니다.
        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(),
                user.getPassword(), // DB에 저장된 암호화된 패스워드
                new ArrayList<>() // TODO: 실제 사용자의 권한(Role) 목록을 설정해야 합니다. 예: Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
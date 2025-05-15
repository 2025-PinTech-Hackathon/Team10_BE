package com.enp.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class SignupRequestDTO {
        private String nickname;
        private String loginId;
        private String password;
    }

package com.v1.iskream.layer.domain.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    private String id;
    private String nickname;
    private String password;
}

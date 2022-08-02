package com.v1.iskream.layer.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private int status;
    private String msg;
    private String token;
}

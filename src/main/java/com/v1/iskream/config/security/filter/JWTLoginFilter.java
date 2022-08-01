package com.v1.iskream.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v1.iskream.config.security.userDtail.UserDetailImpl;
import com.v1.iskream.config.security.jwt.JWTUtil;
import com.v1.iskream.layer.domain.dto.request.LoginRequestDto;
import com.v1.iskream.layer.domain.dto.response.LoginResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/users/login");
    }

    // 검증 과정
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getId(), loginRequestDto.getPassword(), null
            );

            // 프로바이더한테 검증 요청
            return getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        UserDetailImpl userDetail = (UserDetailImpl) authResult.getPrincipal();
        String token =  JWTUtil.getToken(userDetail);
        LoginResponseDto loginResponseDto = new LoginResponseDto(200, "로그인에 성공했습니다.");

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        LoginResponseDto errorResponse = new LoginResponseDto(401, "로그인 정보가 일치하지 않습니다.");
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
    }
}

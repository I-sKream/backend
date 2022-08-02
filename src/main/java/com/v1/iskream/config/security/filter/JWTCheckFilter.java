package com.v1.iskream.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v1.iskream.config.security.UnauthorizedEntryPoint;
import com.v1.iskream.config.security.jwt.JWTUtil;
import com.v1.iskream.config.security.userDtail.UserDetailImpl;
import com.v1.iskream.config.security.userDtail.UserDetailServiceImpl;
import com.v1.iskream.layer.domain.dto.response.LoginResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private UserDetailServiceImpl userDetailsService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserDetailServiceImpl userDetailsService, UnauthorizedEntryPoint unauthorizedEntryPoint) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (bearer == null || !bearer.startsWith("Bearer ")) {
                super.doFilterInternal(request, response, chain);
                return;
            }

            String token = bearer.substring("Bearer ".length());

            String username = JWTUtil.verify(token);

            if (username != "") {
                UsernamePasswordAuthenticationToken userToken = getUserToken(username);
                SecurityContextHolder.getContext().setAuthentication(userToken);
                super.doFilterInternal(request, response, chain);
            } else {
                throw new AuthenticationException("유효하지 않은 토큰입니다.") {};
            }
        }catch (AuthenticationException e){
            unauthorizedEntryPoint.commence(request, response, e);
        }
    }

    private UsernamePasswordAuthenticationToken getUserToken(String username){
        try {
            UserDetailImpl user = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(
                    user.getUser(), null, user.getAuthorities()
            );
        }catch (Exception e){
            throw new AuthenticationException("존재하지 않는 유저입니다."){};
        }
    }
}

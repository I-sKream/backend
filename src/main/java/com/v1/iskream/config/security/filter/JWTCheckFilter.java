package com.v1.iskream.config.security.filter;

import com.v1.iskream.config.security.jwt.JWTUtil;
import com.v1.iskream.config.security.userDtail.UserDetailImpl;
import com.v1.iskream.config.security.userDtail.UserDetailServiceImpl;
import com.v1.iskream.layer.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private UserDetailServiceImpl userDetailsService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserDetailServiceImpl userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(bearer == null || !bearer.startsWith("Bearer ")){
            super.doFilterInternal(request, response, chain);
            return;
        }

        String token = bearer.substring("Bearer ".length());

        String username = JWTUtil.verify(token);

        System.out.println(username);

        if(username != "") {
            UserDetailImpl user = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    user.getUser(), null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            super.doFilterInternal(request, response, chain);
        }else{
            throw new RuntimeException("Token is not valid");
        }
    }
}

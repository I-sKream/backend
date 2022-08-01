package com.v1.iskream.config.security.userDtail;

import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetailImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findOne(username);
        return new UserDetailImpl(user);
    }
}

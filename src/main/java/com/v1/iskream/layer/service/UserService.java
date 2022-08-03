package com.v1.iskream.layer.service;

import com.v1.iskream.config.security.passwordEncoder.PasswordEncoder;
import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.domain.dto.request.SignupRequestDto;
import com.v1.iskream.layer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(SignupRequestDto signupRequestDto){
        checkUserExist(signupRequestDto.getId());
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto.getId(), password, signupRequestDto.getNickname());
        return userRepository.save(user);
    }

    public User findOne(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );
        return user;
    }

    private void checkUserExist(String userId){
        if(userRepository.existsById(userId)) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }
    }
}

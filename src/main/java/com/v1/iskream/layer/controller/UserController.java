package com.v1.iskream.layer.controller;

import com.v1.iskream.layer.domain.dto.request.SignupRequestDto;
import com.v1.iskream.layer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignupRequestDto signupRequestDto){
        userService.save(signupRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

}

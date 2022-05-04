package com.returtless.javadiplom.controller;

import com.returtless.javadiplom.dto.TokenDTO;
import com.returtless.javadiplom.dto.UserDTO;
import com.returtless.javadiplom.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static java.lang.String.format;

@RestController
@Slf4j
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public TokenDTO login(@RequestBody UserDTO userDTO) {
        TokenDTO token = new TokenDTO();
        token.setValue(authService.getToken(userDTO));
        log.info(format("Успешная авторизация пользователя %s", userDTO.getLogin()));
        return token;
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        authService.removeToken(token);
        log.info("Пользователь вышел из системы");
        return new ResponseEntity(HttpStatus.OK);
    }
}

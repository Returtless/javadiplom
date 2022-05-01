package com.returtless.javadiplom.service;

import com.returtless.javadiplom.auth.JwtTokenProvider;
import com.returtless.javadiplom.dto.UserDTO;
import com.returtless.javadiplom.model.Token;
import com.returtless.javadiplom.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    public String getToken(UserDTO userDTO) {
        try {
            var login = userDTO.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, userDTO.getPassword()));
            var user = userService.findByLogin(login);
            return jwtTokenProvider.createToken(login, user.getRoles());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Неизвестные логин или пароль");
        }
    }

    public void removeToken(String token) {
        var removedToken = new Token();
        removedToken.setToken(token);
        tokenRepository.save(removedToken);
    }
}
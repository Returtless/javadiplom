package com.returtless.javadiplom.auth;

import com.returtless.javadiplom.exception.NotFoundException;
import com.returtless.javadiplom.model.User;
import com.returtless.javadiplom.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userService.findByLogin(username);
        JwtUser jwtUser = JwtUserFactory.create(user);
        return jwtUser;
    }
}

package com.returtless.javadiplom.service;

import com.returtless.javadiplom.exception.NotFoundException;
import com.returtless.javadiplom.model.User;
import com.returtless.javadiplom.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final static String USER_LOGIN_NOT_FOUND = "Пользователь [%s] не найден";
    private final static String USER_ID_NOT_FOUND = "Пользователь с id=[%s] не найден";

    private final UserRepository userRepository;

    public User findByLogin(String login) {
        User user = userRepository.findByUsername(login).orElseThrow(() -> {
            log.error(format(USER_LOGIN_NOT_FOUND, login));
            throw new NotFoundException(format(USER_LOGIN_NOT_FOUND, login));
        });
        log.info("Пользователь: {} найден по логину: {}", user, login);
        return user;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error(format(USER_ID_NOT_FOUND, id));
            throw new NotFoundException(format(USER_ID_NOT_FOUND, id));
        });
        log.info("Пользователь: {} найден по идентификатору: {}", user, id);
        return user;
    }

}
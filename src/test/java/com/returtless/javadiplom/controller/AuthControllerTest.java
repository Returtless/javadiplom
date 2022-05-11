package com.returtless.javadiplom.controller;

import com.returtless.javadiplom.dto.TokenDTO;
import com.returtless.javadiplom.dto.UserDTO;
import com.returtless.javadiplom.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AuthControllerTest {

    AuthService authService;

    private final static String TEST = "test";
    private final static String TOKEN = "Bearer test";
    @Before
    public void setup(){
        authService = Mockito.mock(AuthService.class);
        Mockito.when(authService.getToken(new UserDTO(TEST, TEST))).thenReturn(TOKEN);
    }

    @Test
    public void testLogin(){
        TokenDTO actualTokenDTO =
                new AuthController(authService)
                        .login(new UserDTO(TEST, TEST));
        TokenDTO expected = new TokenDTO(TOKEN);
        Assertions.assertEquals(actualTokenDTO, expected);
    }

    @Test
    public void testLogout(){
        Assertions.assertEquals(new ResponseEntity(HttpStatus.OK), new AuthController(authService)
                .logout(TOKEN));
    }
}
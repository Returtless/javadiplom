package com.returtless.javadiplom.controller;

import com.returtless.javadiplom.dto.TokenDTO;
import com.returtless.javadiplom.dto.UserDTO;
import com.returtless.javadiplom.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {

   @Autowired
    private AuthService authService;

    @PostMapping("login")
    public TokenDTO login(@RequestBody UserDTO userDTO) {
        TokenDTO token = new TokenDTO();
        token.setValue(authService.getToken(userDTO));
        return token;
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        authService.removeToken(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestHeader("auth-token") String authToken,
                                  @RequestParam("limit") int limit) throws Exception {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestHeader("auth-token") String authToken,
                                      @RequestParam("filename") String fileName) throws IOException {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> delete(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String fileName) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/file")
    public ResponseEntity<String> rename(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String fileName,
                                         @RequestBody String newFileName) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/file")
    public ResponseEntity<String> upload(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String fileName, MultipartFile file) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

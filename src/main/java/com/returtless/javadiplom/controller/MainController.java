package com.returtless.javadiplom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String login) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        return new ResponseEntity<>(HttpStatus.OK);
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
                                         @RequestBody String newFileName){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/file")
    public ResponseEntity<String> upload(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String fileName, MultipartFile file){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

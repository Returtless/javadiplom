package com.returtless.javadiplom.controller;

import com.returtless.javadiplom.auth.JwtTokenProvider;
import com.returtless.javadiplom.config.CustomAuthenticationProvider;
import com.returtless.javadiplom.config.SecurityConfig;
import com.returtless.javadiplom.dto.FileDTO;
import com.returtless.javadiplom.dto.UserDTO;
import com.returtless.javadiplom.model.File;
import com.returtless.javadiplom.service.FileService;
import com.returtless.javadiplom.service.UserService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTest {
    private MockMvc mockMvc;
    @Mock
    FileService mockFileService;

    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    SecurityConfig securityConfig;
    private FileController controller;

    UserDTO userDTO = new UserDTO("Username", "password");

    @Before
    public void setup() throws Exception {
        controller= new FileController(mockFileService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testList() throws Exception {
        List<FileDTO> testList = new ArrayList<>();
        testList.add(new FileDTO("file1", 10));
        testList.add(new FileDTO("file2", 20));
        testList.add(new FileDTO("file3", 30));
        Mockito.when(mockFileService.getFiles("test", 3))
                .thenReturn(testList);
        List<FileDTO> actual =
                new FileController(mockFileService).getAllFiles("Bearer test",3);

        List<FileDTO> expected = new ArrayList(testList);
        Assertions.assertEquals(actual, expected);
    }

}
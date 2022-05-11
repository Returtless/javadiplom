package com.returtless.javadiplom.service;

import com.returtless.javadiplom.auth.JwtTokenProvider;
import com.returtless.javadiplom.dto.FileDTO;
import com.returtless.javadiplom.exception.FileException;
import com.returtless.javadiplom.exception.NotFoundException;
import com.returtless.javadiplom.model.File;
import com.returtless.javadiplom.model.Status;
import com.returtless.javadiplom.repository.FileCrudRepository;
import com.returtless.javadiplom.repository.FileRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
public class FileServiceTest {
    private static final String TOKEN = "Bearer token";
    private static final String USER = "admin";
    private static final String TEST = "test";

    private static final File FILE = new File(1L, new Date(), new Date(), Status.ACTIVE, TEST, USER, "path", 100L);

    private static FileService fileService;

    @BeforeAll
    public static void init() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        FileCrudRepository fileLocalRepository = Mockito.mock(FileCrudRepository.class);

        Mockito.when(jwtTokenProvider.getUserName(TOKEN.substring(7))).thenReturn(USER);
        Mockito.when(fileLocalRepository.findByUsernameAndStatus(USER, Status.ACTIVE)).thenReturn(Collections.singletonList(FILE));
        Mockito.when(fileLocalRepository.findByUsernameAndNameAndStatus(USER, TEST, Status.ACTIVE))
                .thenReturn(Optional.of(FILE));

        fileService = new FileService(fileRepository, jwtTokenProvider, fileLocalRepository);
    }

    @Test
    void getFilesThenReturnsListTest() {
        List<FileDTO> filesDTO = fileService.getFiles(TOKEN, 10);
        assertEquals(filesDTO.size(), 1);
        assertEquals(TEST, filesDTO.get(0).getFilename());
    }

    @Test
    void getFileThenReturnFileWithRightPropTest() {
        java.io.File javaFile = fileService.getFile(TOKEN, TEST);
        assertEquals(TEST, javaFile.getName());
        assertEquals("path/test", javaFile.getPath());
    }

    @Test
    void renameFileThenExceptionThrowsTest() {
        Throwable thrown = assertThrows(NotFoundException.class, () -> fileService.renameFile(TOKEN, "", ""));
        assertNotNull(thrown.getMessage());
    }

    @Test
    void uploadFileThenExceptionThrowsTest() {
        Throwable thrown = assertThrows(FileException.class, () -> fileService.uploadFile(TOKEN, null, ""));
        assertNotNull(thrown.getMessage());
    }
}
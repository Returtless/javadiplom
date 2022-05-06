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
    private static String token = "Bearer token";
    private static String user = "admin";
    private static FileService fileService;
    private static File file = new File(1L, new Date(), new Date(), Status.ACTIVE, "test", user, "path", 100L);

    @BeforeAll
    public static void init() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        FileCrudRepository fileLocalRepository = Mockito.mock(FileCrudRepository.class);

        Mockito.when(jwtTokenProvider.getUserName(token.substring(7))).thenReturn(user);
        Mockito.when(fileLocalRepository.findByUsernameAndStatus(user, Status.ACTIVE)).thenReturn(Collections.singletonList(file));
        Mockito.when(fileLocalRepository.findByUsernameAndNameAndStatus(user, "test", Status.ACTIVE))
                .thenReturn(Optional.of(file));

        fileService = new FileService(fileRepository, jwtTokenProvider, fileLocalRepository);
    }

    @Test
    void whenGetFilesThenReturnsList() {
        List<FileDTO> filesDTO = fileService.getFiles(token, 10);
        assertEquals(filesDTO.size(), 1);
        assertEquals("test", filesDTO.get(0).getFilename());
    }

    @Test
    void whenGetFileThenReturnFileWithRightProp() {
        java.io.File javaFile = fileService.getFile(token, "test");
        assertEquals("test", javaFile.getName());
        assertEquals("path/test", javaFile.getPath());
    }

    @Test
    void whenRenameFileThenExceptionThrows() {
        Throwable thrown = assertThrows(NotFoundException.class, () -> fileService.renameFile(token, "", ""));
        assertNotNull(thrown.getMessage());
    }

    @Test
    void whenUploadFileThenExceptionThrows() {
        Throwable thrown = assertThrows(FileException.class, () -> fileService.uploadFile(token, null, ""));
        assertNotNull(thrown.getMessage());
    }
}
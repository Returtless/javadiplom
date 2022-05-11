package com.returtless.javadiplom.controller;

import com.returtless.javadiplom.auth.JwtTokenProvider;
import com.returtless.javadiplom.dto.FileDTO;
import com.returtless.javadiplom.dto.RenameFileDTO;
import com.returtless.javadiplom.dto.UserDTO;
import com.returtless.javadiplom.model.File;
import com.returtless.javadiplom.model.Status;
import com.returtless.javadiplom.repository.FileCrudRepository;
import com.returtless.javadiplom.repository.FileRepository;
import com.returtless.javadiplom.service.AuthService;
import com.returtless.javadiplom.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class FileControllerTest {

    FileService fileService;
    AuthService authService;
    FileRepository fileRepository;
    FileCrudRepository fileLocalRepository;

    List<FileDTO> testList;
    List<File> fileList;

    private final static String TEST = "test";
    private final static String TOKEN = "Bearer test";

    @Before
    public void setup() {

        testList = new ArrayList<>();
        testList.add(new FileDTO("file1", 10));
        testList.add(new FileDTO("file2", 20));
        testList.add(new FileDTO("file3", 30));

        fileList = new ArrayList<>();
        fileList.add(new File(1, new Date(), new Date(), Status.ACTIVE, "file1", TEST, TEST, 10));
        fileList.add(new File(2, new Date(), new Date(), Status.ACTIVE, "file2", TEST, TEST, 20));
        fileList.add(new File(3, new Date(), new Date(), Status.ACTIVE, "file3", TEST, TEST, 30));

        fileRepository = Mockito.mock(FileRepository.class);
        JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        fileLocalRepository = Mockito.mock(FileCrudRepository.class);

        fileService = new FileService(fileRepository, jwtTokenProvider, fileLocalRepository);
        Mockito.when(jwtTokenProvider.getUserName(TEST)).thenReturn(TEST);

        authService = Mockito.mock(AuthService.class);
        Mockito.when(authService.getToken(new UserDTO(TEST, TEST))).thenReturn(TOKEN);
    }

    @Test
    public void getAllFilesTest() {
        Mockito.when(fileLocalRepository.findByUsernameAndStatus(TEST, Status.ACTIVE)).thenReturn(fileList);
        List<FileDTO> actual = new FileController(fileService).getAllFiles(TOKEN, 3);
        List<FileDTO> expected = new ArrayList(testList);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void deleteTest() {
        Mockito.when(fileLocalRepository.findByUsernameAndNameAndStatus(TEST, TEST, Status.ACTIVE)).thenReturn(Optional.of(fileList.get(0)));
        Assertions.assertDoesNotThrow(() -> new FileController(fileService).delete(TOKEN, TEST));
    }

    @Test
    public void renameTest() {
        Mockito.when(fileLocalRepository.findByUsernameAndNameAndStatus(TEST, TEST, Status.ACTIVE)).thenReturn(Optional.of(fileList.get(0)));
        Mockito.when(fileRepository.renameFile(TEST, TEST, "new test")).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> new FileController(fileService).rename(TOKEN, TEST, new RenameFileDTO("new test")));
    }

    @Test
    public void uploadTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(TEST, TEST.getBytes());
        Mockito.when(fileRepository.saveFile(multipartFile, TEST, "null/test/")).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> new FileController(fileService).upload(TOKEN, TEST, multipartFile));
    }

    @Test
    public void downloadTest() {
        /*Mockito.when(fileLocalRepository.findByUsernameAndNameAndStatus(TEST, TEST, Status.ACTIVE))
                .thenReturn(Optional.of(new File(1, new Date(), new Date(), Status.ACTIVE, "file1", TEST, TEST, 10)));
        java.io.File file = new java.io.File("file1");
        Assertions.assertDoesNotThrow(() -> new FileController(fileService).download(TOKEN, TEST));*/
    }
}
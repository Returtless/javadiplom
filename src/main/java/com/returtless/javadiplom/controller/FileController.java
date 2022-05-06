package com.returtless.javadiplom.controller;

import com.returtless.javadiplom.dto.FileDTO;
import com.returtless.javadiplom.dto.RenameFileDTO;
import com.returtless.javadiplom.exception.NotFoundException;
import com.returtless.javadiplom.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public List<FileDTO> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
        return fileService.getFiles(authToken, limit);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> download(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) throws IOException {
        File file = fileService.getFile(authToken, fileName);
        if (file.exists()) {
            Path path = Paths.get(file.getAbsolutePath());
            byte[] bytes = Files.readAllBytes(path);
            String probeContentType = Files.probeContentType(path);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(file.getName()).build().toString()).contentType(probeContentType != null ? MediaType.valueOf(probeContentType) : MediaType.APPLICATION_OCTET_STREAM).body(bytes);
        } else {
            log.error(format("Файл %s отсутствует", fileName));
            throw new NotFoundException("Отсутствует файл для скачивания");
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> delete(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        fileService.deleteFile(authToken, fileName);
        log.info(format("Файл %s успешно удален", fileName));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/file")
    public ResponseEntity<?> rename(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, @RequestBody RenameFileDTO renameFile) {
        fileService.renameFile(authToken, fileName, renameFile.getNewName());
        log.info(format("Название файла изменено на %s", renameFile.getNewName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/file")
    public ResponseEntity<?> upload(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, MultipartFile file) {
        fileService.uploadFile(authToken, file, fileName);
        log.info(format("Файл %s успешно загружен", fileName));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

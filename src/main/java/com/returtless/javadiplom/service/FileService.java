package com.returtless.javadiplom.service;

import com.returtless.javadiplom.auth.JwtTokenProvider;
import com.returtless.javadiplom.dto.FileDTO;
import com.returtless.javadiplom.exception.FileException;
import com.returtless.javadiplom.exception.NotFoundException;
import com.returtless.javadiplom.model.File;
import com.returtless.javadiplom.model.Status;
import com.returtless.javadiplom.repository.FileCrudRepository;
import com.returtless.javadiplom.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.returtless.javadiplom.auth.JwtTokenProvider.BEARER_LENGTH;
import static java.lang.String.format;

@Service
@Slf4j
public class FileService {
    @Value("${files.path}")
    private String path;
    private final static String FULL_PATH = "%s/%s/";

    private final FileRepository fileRepository;
    private final JwtTokenProvider tokenProvider;
    private final FileCrudRepository fileCrudRepository;


    @Autowired
    public FileService(FileRepository fileRepository, JwtTokenProvider tokenProvider, FileCrudRepository fileCrudRepository) {
        this.fileRepository = fileRepository;
        this.tokenProvider = tokenProvider;
        this.fileCrudRepository = fileCrudRepository;
    }

    @PostConstruct
    private void init() {
        Path checkPath = Paths.get(path);
        if (!Files.exists(checkPath)) {
            java.io.File file = new java.io.File(path);
            file.mkdir();
        }
    }

    public List<FileDTO> getFiles(String token, int limit) {
        String username = getUsername(token);
        List<File> files = fileCrudRepository.findByUsernameAndStatus(username, Status.ACTIVE);
        return files.stream().limit(limit).map(this::convertFromFile).collect(Collectors.toList());
    }

    public java.io.File getFile(String token, String filename) {
        String username = getUsername(token);
        String fullPath = fileCrudRepository.findByUsernameAndNameAndStatus(username, filename, Status.ACTIVE).orElseThrow(() -> new NotFoundException(format("Файл с именем [%s] не найден.", filename))).getPath();
        return new java.io.File(fullPath + "/" + filename);
    }

    public void renameFile(String token, String filename, String newName) {
        String username = getUsername(token);
        File file = fileCrudRepository.findByUsernameAndNameAndStatus(username, filename, Status.ACTIVE).orElseThrow(() -> new NotFoundException(format("Файл с именем [%s] не найден.", filename)));
        if (fileRepository.renameFile(filename, file.getPath(), newName)) {
            file.setName(newName);
            fileCrudRepository.save(file);
        } else {
            log.error("Не удалось переименовать файл : %s, данный файл не существует");
            throw new FileException("Не удалось переименовать файл");
        }
    }

    public void uploadFile(String token, MultipartFile multipartFile, String fileName) {
        String username = getUsername(token);
        String fullPath = format(FULL_PATH, path, username);
        try {
            if (fileRepository.saveFile(multipartFile, fileName, fullPath)) {
                Date now = new Date(System.currentTimeMillis());
                File file = File.builder().name(fileName).path(fullPath).username(username).size(multipartFile.getBytes().length).created(now).updated(now).status(Status.ACTIVE).build();
                fileCrudRepository.save(file);
            } else {
                log.error("Не удалось загрузить файл");
                throw new FileException("Не удалось загрузить файл");
            }
        } catch (IOException e) {
            log.error(format("Не удалось загрузить файл : %s", e.getMessage()));
            throw new FileException("не удалось загрузить файл");
        }
    }

    public void deleteFile(String token, String filename) {
        String username = getUsername(token);
        String fullPath = format(FULL_PATH, path, username);
        if (fileRepository.deleteFile(filename, fullPath)) {
            File file = fileCrudRepository.findByUsernameAndNameAndStatus(username, filename, Status.ACTIVE).orElseThrow(() -> {
                log.error(format("Файл с именем [%s] не найден.", filename));
                throw new FileException(format("Файл с именем [%s] не найден.", filename));
            });
            file.setStatus(Status.DELETED);
            fileCrudRepository.save(file);
        }
    }

    private String getUsername(String token) {
        return tokenProvider.getUserName(token.substring(BEARER_LENGTH));
    }

    private FileDTO convertFromFile(File file) {
        return FileDTO.builder().filename(file.getName()).size((int) file.getSize()).build();
    }
}

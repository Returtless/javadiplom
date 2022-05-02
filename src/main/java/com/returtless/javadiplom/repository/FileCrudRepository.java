package com.returtless.javadiplom.repository;

import com.returtless.javadiplom.model.File;
import com.returtless.javadiplom.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FileCrudRepository extends CrudRepository<File, Long> {

    List<File> findByUsernameAndStatus(String username, Status status);

    Optional<File> findByUsernameAndNameAndStatus(String username, String name, Status status);
}

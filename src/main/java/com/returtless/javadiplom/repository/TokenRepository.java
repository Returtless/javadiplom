package com.returtless.javadiplom.repository;

import com.returtless.javadiplom.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
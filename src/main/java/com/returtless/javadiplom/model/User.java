package com.returtless.javadiplom.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;

@Entity
@Data
public class User {
    @Id
    private String username;
    private String password;
}
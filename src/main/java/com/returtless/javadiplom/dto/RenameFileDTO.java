package com.returtless.javadiplom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RenameFileDTO {

    @JsonProperty("filename")
    private String newName;
}

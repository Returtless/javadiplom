package com.returtless.javadiplom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileDTO {
    private String filename;
    private int size;
}

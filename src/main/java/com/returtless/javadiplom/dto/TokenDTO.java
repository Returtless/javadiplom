package com.returtless.javadiplom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {
    @JsonProperty("auth-token")
    private String value;
}

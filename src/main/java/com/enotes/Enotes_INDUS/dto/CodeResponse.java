package com.enotes.Enotes_INDUS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodeResponse {
    private String output;
    private String error;
    private long executionTime;
}

package com.home.utilities.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private boolean success;
    private String message;
}

package com.home.utilities.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexDetails {

    private Long id;
    private Long clientId;
    private Double value;
    private Instant modifiedAt;
}

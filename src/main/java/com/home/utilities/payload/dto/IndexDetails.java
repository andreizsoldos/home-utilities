package com.home.utilities.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexDetails {

    private Long id;
    private Long clientId;
    private Double value;
    private Instant createdAt;
    private Instant modifiedAt;
}

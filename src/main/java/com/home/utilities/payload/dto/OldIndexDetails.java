package com.home.utilities.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OldIndexDetails {

    private Long id;
    private Long indexId;
    private Double value;
    private Instant createdAt;
    private Instant modifiedAt;
}

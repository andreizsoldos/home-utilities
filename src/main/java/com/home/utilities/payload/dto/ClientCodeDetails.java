package com.home.utilities.payload.dto;

import com.home.utilities.entities.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCodeDetails {

    private Long id;
    private String clientNumber;
    private String clientName;
    private Branch branch;
    private String consumptionLocationNumber;
    private String consumptionAddress;
    private LocalDate contractDate;

    public Long count(final List<IndexDetails> indexesList) {
        return indexesList.stream()
              .filter(i -> i.getClientId().equals(id))
              .count();
    }
}

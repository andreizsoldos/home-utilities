package com.home.utilities.payload.dto;

import com.home.utilities.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Getter
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

    public LocalDate lastIndexDate(final List<IndexDetails> indexesList) {
        return indexesList.stream()
              .filter(i -> i.getClientId().equals(id))
              .map(IndexDetails::getCreatedAt)
              .map(i -> LocalDate.ofInstant(i, ZoneId.systemDefault()))
              .max(LocalDate::compareTo)
              .orElse(null);
    }
}

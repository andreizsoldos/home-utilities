package com.home.utilities.services;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.Index;
import com.home.utilities.payload.dto.IndexDetails;
import com.home.utilities.payload.request.IndexRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface IndexService {

    Optional<Index> findById(Long indexId);

    Optional<Index> createIndex(IndexRequest request, Long clientId);

    Optional<Index> updateIndex(Double newValue, Long indexId);

    List<IndexDetails> getIndexes(Branch branch, Long userId);

    Optional<Double> getLastIndexValue(Long clientId, Branch branch, Long userId);

    Optional<String> getLastIndexValue(Branch branch, Long userId);

    Optional<String> getLastModifiedDate(Branch branch, Long userId);

    LocalDate getLastCreatedDate(Branch branch, Long userId);

    LocalDate firstDayOfCurrentWeek();

    LocalDate lastDayOfCurrentWeek();

    LocalDate firstDayOfCurrentMonth();

    LocalDate lastDayOfCurrentMonth();

    Integer lastDayValueOfCurrentMonth();

    Map<String, Double> getIndexValuesForCurrentWeek(Long clientId, Branch branch, Long userId, Locale locale);

    Map<Integer, Double> getIndexValuesForCurrentMonth(Long clientId, Branch branch, Long userId);

    Map<String, Double> getMonthlyMinIndexValues(Long clientId, Branch branch, Long userId, Locale locale);

    Map<String, Double> getMonthlyMaxIndexValues(Long clientId, Branch branch, Long userId, Locale locale);
}

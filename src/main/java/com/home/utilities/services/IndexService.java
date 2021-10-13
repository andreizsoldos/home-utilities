package com.home.utilities.services;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.Index;
import com.home.utilities.entities.ValueRange;
import com.home.utilities.payload.dto.IndexDetails;
import com.home.utilities.payload.dto.OldIndexDetails;
import com.home.utilities.payload.request.NewIndexRequest;
import com.home.utilities.payload.request.OldIndexRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface IndexService {

    Optional<Index> findById(Long indexId);

    Optional<Index> createIndex(NewIndexRequest request, Long clientId);

    Optional<Index> updateIndex(OldIndexRequest request, Long indexId);

    List<IndexDetails> getIndexes(Branch branch, Long userId);

    void saveOldIndex(OldIndexRequest request, Long indexId);

    List<OldIndexDetails> getOldIndexes(List<Long> indexId);

    Optional<Double> getLastIndexValue(Long clientId, Branch branch, Long userId);

    Optional<String> getLastIndexValue(Branch branch, Long userId);

    Optional<String> getLastModifiedDate(Branch branch, Long userId);

    Long getLastModificationDuration(Branch branch, Long userId);

    LocalDate getFirstCreatedDate(Long clientId, Branch branch, Long userId);

    LocalDate getLastCreatedDate(Branch branch, Long userId);

    Optional<LocalDate> getLastCreatedIndexDate(Double lastIndex, Long clientId, Branch branch, Long userId);

    LocalDate firstDayOfCurrentWeek();

    LocalDate lastDayOfCurrentWeek();

    List<Integer> currentDaysOfWeek();

    LocalDate firstDayOfCurrentMonth();

    LocalDate lastDayOfCurrentMonth();

    Integer lastDayValueOfCurrentMonth();

    Optional<Double> getPreviousLastIndex(Long clientId, Branch branch, Long userId, LocalDate beforeDate);

    Map<String, Double> getIndexValuesForCurrentWeek(Long clientId, Branch branch, Long userId, Locale locale);

    Map<Integer, Double> getIndexValuesForCurrentMonth(Long clientId, Branch branch, Long userId);

    Map<String, Double> getMonthlyMinIndexValues(Long clientId, Branch branch, Long userId, Locale locale);

    Map<String, Double> getMonthlyMaxIndexValues(Long clientId, Branch branch, Long userId, Locale locale);

    Map<String, Double> getConsumptionValues(ValueRange valueRange, Long clientId, Branch branch, Long userId, Locale locale);
}

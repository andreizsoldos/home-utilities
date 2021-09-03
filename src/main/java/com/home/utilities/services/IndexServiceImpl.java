package com.home.utilities.services;

import com.home.utilities.entities.*;
import com.home.utilities.entities.audit.BaseEntity;
import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.payload.dto.IndexDetails;
import com.home.utilities.payload.dto.OldIndexDetails;
import com.home.utilities.payload.request.NewIndexRequest;
import com.home.utilities.payload.request.OldIndexRequest;
import com.home.utilities.repository.ClientCodeRepository;
import com.home.utilities.repository.IndexRepository;
import com.home.utilities.repository.OldIndexRepository;
import com.home.utilities.services.util.DateTimeConverter;
import com.home.utilities.services.util.Translation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private static final List<Integer> MONTHS_IN_YEAR = Arrays.stream(Month.values())
          .map(Month::getValue)
          .collect(Collectors.toList());

    private final Translation translation;
    private final IndexRepository indexRepository;
    private final OldIndexRepository oldIndexRepository;
    private final ClientCodeRepository clientCodeRepository;


    @Override
    public Optional<Index> findById(final Long indexId) {
        return indexRepository.findById(indexId);
    }

    @Override
    public Optional<Index> createIndex(final NewIndexRequest request, final Long clientId) {
        final var clientCode = clientCodeRepository.findById(clientId)
              .orElseThrow(() -> new NotFoundException("Client code", "id", clientId));
        final var oldIndex = new OldIndex();
        final var index = new Index();
        index.setValue(request.getValue());
        index.setClientCode(clientCode);
        oldIndex.setValue(request.getValue());
        oldIndex.setIndex(index);
        oldIndexRepository.save(oldIndex);
        return Optional.of(indexRepository.save(index));
    }

    @Override
    public Optional<Index> updateIndex(final OldIndexRequest request, final Long indexId) {
        final var index = indexRepository.findById(indexId)
              .orElseThrow(() -> new NotFoundException("Index", "id", indexId));
        index.setValue(request.getValue());
        return Optional.of(indexRepository.save(index));
    }

    @Override
    public List<IndexDetails> getIndexes(final Branch branch, final Long userId) {
        return indexRepository.findIndexes(branch, userId).stream()
              .map(i -> new IndexDetails(i.getId(), i.getClientCode().getId(), i.getValue(), i.getCreatedAt(), i.getModifiedAt()))
              .collect(Collectors.toList());
    }

    @Override
    public void saveOldIndex(final OldIndexRequest request, final Long indexId) {
        final var index = indexRepository.findById(indexId)
              .orElseThrow(() -> new NotFoundException("Index", "id", indexId));
        final var oldIndex = new OldIndex();
        oldIndex.setValue(request.getValue());
        oldIndex.setIndex(index);
        oldIndexRepository.save(oldIndex);
    }

    @Override
    public List<OldIndexDetails> getOldIndexes(final List<Long> indexesId) {
        return oldIndexRepository.findByIndexesId(indexesId).stream()
              .map(i -> new OldIndexDetails(i.getId(), i.getIndex().getId(), i.getValue(), i.getCreatedAt(), i.getModifiedAt()))
              .collect(Collectors.toList());
    }

    @Override
    public Optional<Double> getLastIndexValue(final Long clientId, final Branch branch, final Long userId) {
        return indexRepository.findLastIndexValue(clientId, branch, userId);
    }

    @Override
    public Optional<String> getLastIndexValue(final Branch branch, final Long userId) {
        return indexRepository.findLastIndexValue(branch, userId)
              .map(String::valueOf);
    }

    @Override
    public Optional<String> getLastModifiedDate(final Branch branch, final Long userId) {
        return indexRepository.findLastModifiedDate(branch, userId)
              .map(d -> " → " + d.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @Override
    public LocalDate getLastCreatedDate(final Branch branch, final Long userId) {
        return indexRepository.findLastCreatedDate(branch, userId)
              .map(i -> LocalDate.ofInstant(i, ZoneId.systemDefault()))
              .orElse(LocalDate.now(ZoneId.systemDefault()).minusDays(1L));
    }

    @Override
    public Optional<LocalDate> getLastCreatedDate(final Double lastIndex) {
        return indexRepository.findByValue(lastIndex)
              .map(BaseEntity::getCreatedAt)
              .map(i -> LocalDate.ofInstant(i, ZoneId.systemDefault()));
    }

    @Override
    public LocalDate firstDayOfCurrentWeek() {
        return LocalDate.now(ZoneId.systemDefault())
              .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    @Override
    public LocalDate lastDayOfCurrentWeek() {
        return LocalDate.now(ZoneId.systemDefault())
              .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    @Override
    public List<Integer> currentDaysOfWeek() {
        return IntStream.rangeClosed(0, 6)
              .boxed()
              .map(i -> firstDayOfCurrentWeek().plusDays(i))
              .map(LocalDate::getDayOfMonth)
              .collect(Collectors.toList());
    }

    @Override
    public LocalDate firstDayOfCurrentMonth() {
        return LocalDate.now(ZoneId.systemDefault()).withDayOfMonth(1);
    }

    @Override
    public LocalDate lastDayOfCurrentMonth() {
        return LocalDate.now(ZoneId.systemDefault()).withDayOfMonth(lastDayValueOfCurrentMonth());
    }

    @Override
    public Integer lastDayValueOfCurrentMonth() {
        return LocalDate.now(ZoneId.systemDefault()).lengthOfMonth();
    }

    @Override
    public Map<String, Double> getIndexValuesForCurrentWeek(final Long clientId, final Branch branch, final Long userId, final Locale locale) {
        final var indexValuesOfThisWeek = indexRepository.findIndexes(branch, userId).stream()
              .filter(i -> filterDates(i, firstDayOfCurrentWeek(), lastDayOfCurrentWeek()))
              .filter(i -> i.getClientCode().getId().equals(clientId))
              .collect(Collectors.toMap(i -> getDayOfWeekName(i, locale), Index::getValue, (v1, v2) -> v1));

        return Arrays.stream(DaysOfWeek.values())
              .collect(Collectors.toMap(d -> translation.getMessage(d.description(), locale), d -> indexValuesOfThisWeek.getOrDefault(translation.getMessage(d.description(), locale), 0D), (v1, v2) -> v1, LinkedHashMap::new));
    }

    @Override
    public Map<Integer, Double> getIndexValuesForCurrentMonth(final Long clientId, final Branch branch, final Long userId) {
        final var indexValuesOfThisMonth = indexRepository.findIndexes(branch, userId).stream()
              .filter(i -> filterDates(i, firstDayOfCurrentMonth(), lastDayOfCurrentMonth()))
              .filter(i -> i.getClientCode().getId().equals(clientId))
              .collect(Collectors.toMap(this::getDayOfMonthValue, Index::getValue, (v1, v2) -> v1));

        return IntStream.rangeClosed(1, lastDayValueOfCurrentMonth())
              .boxed()
              .collect(Collectors.toMap(d -> d, d -> indexValuesOfThisMonth.getOrDefault(d, 0D), (v1, v2) -> v1, LinkedHashMap::new));
    }

    @Override
    public Map<String, Double> getMonthlyMinIndexValues(final Long clientId, final Branch branch, final Long userId, final Locale locale) {
        final var minIndexValues = indexRepository.findMinIndexValues(branch, userId, MONTHS_IN_YEAR).stream()
              .filter(i -> i.getClientCode().getId().equals(clientId))
              .collect(Collectors.toMap(i -> getDayOfMonthName(i, locale), Index::getValue, (v1, v2) -> v1));

        return Arrays.stream(MonthsOfYear.values())
              .collect(Collectors.toMap(d -> translation.getMessage(d.description(), locale), d -> minIndexValues.getOrDefault(translation.getMessage(d.description(), locale), 0D), (v1, v2) -> v1, LinkedHashMap::new));
    }

    @Override
    public Map<String, Double> getMonthlyMaxIndexValues(final Long clientId, final Branch branch, final Long userId, final Locale locale) {
        final var maxIndexValues = indexRepository.findMaxIndexValues(branch, userId, MONTHS_IN_YEAR).stream()
              .filter(i -> i.getClientCode().getId().equals(clientId))
              .collect(Collectors.toMap(i -> getDayOfMonthName(i, locale), Index::getValue, (v1, v2) -> v1));

        return Arrays.stream(MonthsOfYear.values())
              .collect(Collectors.toMap(d -> translation.getMessage(d.description(), locale), d -> maxIndexValues.getOrDefault(translation.getMessage(d.description(), locale), 0D), (v1, v2) -> v1, LinkedHashMap::new));
    }

    private boolean filterDates(final Index index, final LocalDate fromDate, final LocalDate toDate) {
        final var currentDate = LocalDate.ofInstant(index.getModifiedAt(), ZoneId.systemDefault());
        return DateTimeConverter.isDateInBetween(currentDate, fromDate, toDate);
    }

    private String getDayOfWeekName(final Index index, final Locale locale) {
        final var name = LocalDate.ofInstant(index.getModifiedAt(), ZoneId.systemDefault()).getDayOfWeek().name();
        return translation.getMessage(DaysOfWeek.valueOf(name.toUpperCase()).description(), locale);
    }

    private String getDayOfMonthName(final Index index, final Locale locale) {
        final var name = LocalDate.ofInstant(index.getModifiedAt(), ZoneId.systemDefault()).getMonth().name();
        return translation.getMessage(MonthsOfYear.valueOf(name.toUpperCase()).description(), locale);
    }

    private Integer getDayOfMonthValue(final Index index) {
        return LocalDate.ofInstant(index.getModifiedAt(), ZoneId.systemDefault()).getDayOfMonth();
    }
}

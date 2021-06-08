package com.home.utilities.service;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.Index;
import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.payload.dto.IndexDetails;
import com.home.utilities.payload.request.IndexRequest;
import com.home.utilities.repository.ClientCodeRepository;
import com.home.utilities.repository.IndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;
    private final ClientCodeRepository clientCodeRepository;

    @Override
    public Optional<Index> createIndex(final IndexRequest request, final Long clientId) {
        final var clientCode = clientCodeRepository.findById(clientId)
              .orElseThrow(() -> new NotFoundException("Client code", "id", clientId));
        final var index = new Index();
        index.setValue(request.getValue());
        index.setClientCode(clientCode);
        return Optional.of(indexRepository.save(index));
    }

    @Override
    public List<IndexDetails> getIndexes(final Branch branch, final Long userId) {
        return indexRepository.findIndexes(branch, userId).stream()
              .map(i -> new IndexDetails(i.getId(), i.getClientCode().getId(), i.getValue(), i.getCreatedAt()))
              .collect(Collectors.toList());
    }

    @Override
    public Optional<Double> getLastIndexValue(final Long clientId, final Branch branch, final Long userId) {
        return indexRepository.findLastIndexValue(clientId, branch, userId);
    }

    @Override
    public Optional<Double> getLastIndexValue(final Branch branch, final Long userId) {
        return indexRepository.findLastIndexValue(branch, userId);
    }

    @Override
    public Long getLastModifiedDate(final Branch branch, final Long userId) {
        final var today = Instant.now();
        final var lastDate = indexRepository.findLastModifiedDate(branch, userId)
              .orElse(Instant.now().plus(1L, ChronoUnit.DAYS));
        return Duration.between(lastDate, today).toDaysPart();
    }
}

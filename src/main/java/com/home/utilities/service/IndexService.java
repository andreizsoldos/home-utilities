package com.home.utilities.service;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.Index;
import com.home.utilities.payload.dto.IndexDetails;
import com.home.utilities.payload.request.IndexRequest;

import java.util.List;
import java.util.Optional;

public interface IndexService {

    Optional<Index> createIndex(IndexRequest request, Long clientId);

    List<IndexDetails> getIndexes(Branch branch, Long userId);

    Optional<Long> getLastIndexValue(Long clientId, Branch branch, Long userId);
}

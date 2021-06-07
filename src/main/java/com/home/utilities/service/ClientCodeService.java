package com.home.utilities.service;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.ClientCode;
import com.home.utilities.payload.dto.ClientCodeDetails;
import com.home.utilities.payload.request.ClientCodeRequest;

import java.util.List;
import java.util.Optional;

public interface ClientCodeService {

    Optional<ClientCode> createClientCode(ClientCodeRequest request, Branch branch, Long userId);

    List<ClientCodeDetails> getClientCodes(List<Branch> branches, Long userId);

    Optional<ClientCodeDetails> findFirstClientCode(List<Branch> branches, Long userId);
}

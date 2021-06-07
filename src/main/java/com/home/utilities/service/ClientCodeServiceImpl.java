package com.home.utilities.service;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.ClientCode;
import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.payload.dto.ClientCodeDetails;
import com.home.utilities.payload.request.ClientCodeRequest;
import com.home.utilities.repository.ClientCodeRepository;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientCodeServiceImpl implements ClientCodeService {

    private final ClientCodeRepository clientCodeRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<ClientCode> createClientCode(final ClientCodeRequest request, final Branch branch, final Long userId) {
        final var user = userRepository.findById(userId)
              .orElseThrow(() -> new NotFoundException("User", "id", userId));
        final var clientCode = new ClientCode();
        clientCode.setClientNumber(request.getClientNumber());
        clientCode.setClientName(request.getClientName());
        clientCode.setConsumptionLocationNumber(request.getConsumptionLocationNumber());
        clientCode.setConsumptionAddress(request.getConsumptionAddress());
        clientCode.setContractDate(request.getContractDate());
        clientCode.setBranch(branch);
        clientCode.setUser(user);
        return Optional.of(clientCodeRepository.save(clientCode));
    }

    @Override
    public List<ClientCodeDetails> getClientCodes(final List<Branch> branches, final Long userId) {
        return clientCodeRepository.findByBranchAndUserId(branches, userId);
    }

    @Override
    public Optional<ClientCodeDetails> findFirstClientCode(final List<Branch> branches, final Long userId) {
        return getClientCodes(branches, userId).stream().findFirst();
    }
}

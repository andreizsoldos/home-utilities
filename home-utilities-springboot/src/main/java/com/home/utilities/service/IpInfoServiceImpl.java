package com.home.utilities.service;

import com.home.utilities.entity.IpInfo;
import com.home.utilities.repository.IpInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IpInfoServiceImpl implements IpInfoService {

    private static final String USERNAME = "username";
    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    private static final String IP_INFO_BASE_URL = "https://ipinfo.io/";

    private final RestTemplate restTemplate;
    private final IpInfoRepository ipInfoRepository;

    @Override
    public String getIpAddress(final HttpServletRequest request) {
        final var xfHeader = request.getHeader(X_FORWARDED_FOR);
        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }

    @Override
    public IpInfo createIpInfo(final HttpServletRequest request) {
        final var email = request.getParameter(USERNAME);
        final var ip = this.getIpAddress(request);
        final var infoIpResponse = restTemplate.getForObject(IP_INFO_BASE_URL + ip, IpInfo.class);
        if (infoIpResponse != null) {
            infoIpResponse.setEmail(email);
            infoIpResponse.setFailedIpAttempts(0);
            infoIpResponse.setEndLockTime(null);
            infoIpResponse.setTotalIpAttempts(0L);
            return ipInfoRepository.save(infoIpResponse);
        }
        return ipInfoRepository.save(new IpInfo(email, 0, null, 0L));
    }
}

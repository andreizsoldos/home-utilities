package com.home.utilities.repository;

import com.home.utilities.entities.IpInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IpInfoRepository extends JpaRepository<IpInfo, Long> {

    Optional<IpInfo> findByIp(String ipAddress);
}

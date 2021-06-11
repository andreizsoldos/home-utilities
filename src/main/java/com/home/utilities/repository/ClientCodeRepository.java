package com.home.utilities.repository;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.ClientCode;
import com.home.utilities.payload.dto.ClientCodeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientCodeRepository extends JpaRepository<ClientCode, Long> {

    @Query("""
          SELECT NEW com.home.utilities.payload.dto.ClientCodeDetails(cc.id, cc.clientNumber, cc.clientName, cc.branch, cc.consumptionLocationNumber, cc.consumptionAddress, cc.contractDate)
          FROM ClientCode cc
          INNER JOIN cc.user u
          ON u.id = :userId
          AND cc.branch in (:branches)
          """)
    List<ClientCodeDetails> findByBranchAndUserId(@Param("branches") List<Branch> branches, @Param("userId") Long userId);

    @Query("""
          SELECT count(cc.id) as totalNumberOfClientCodes
          FROM ClientCode cc
          INNER JOIN cc.user u
          ON u.id = :userId
          AND cc.branch = :branch
          """)
    Long getTotalNumberOfClientCodes(@Param("branch") Branch branch, @Param("userId") Long userId);

    @Query("""
          SELECT client.clientName as name FROM Index index
          INNER JOIN index.clientCode client
          WHERE index.id =
              (SELECT max(i.id) from Index i
              INNER JOIN i.clientCode cc
              INNER JOIN cc.user u
              ON u.id = :userId
              AND cc.branch = :branch)
          """)
    Optional<String> getClientCodeNameWhoInsertedLastIndex(@Param("branch") Branch branch, @Param("userId") Long userId);

    int deleteByBranchAndIdAndUserId(Branch branch, Long clientId, Long userId);
}

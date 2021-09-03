package com.home.utilities.repository;

import com.home.utilities.entities.Branch;
import com.home.utilities.entities.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IndexRepository extends JpaRepository<Index, Long> {

    @Query("""
          SELECT i FROM Index i
          INNER JOIN i.clientCode cc
          ON cc.branch = :branch
          INNER JOIN cc.user u
          ON u.id = :userId
          ORDER BY i.createdAt DESC
          """)
    List<Index> findIndexes(@Param("branch") Branch branch, @Param("userId") Long userId);

    Optional<Index> findByValue(Double value);

    @Query("""
          SELECT index.value FROM Index index
          WHERE index.id =
              (SELECT max(i.id) from Index i
              INNER JOIN i.clientCode cc
              ON cc.id = :clientId
              INNER JOIN cc.user u
              ON u.id = :userId
              AND cc.branch = :branch)
          """)
    Optional<Double> findLastIndexValue(@Param("clientId") Long clientId, @Param("branch") Branch branch, @Param("userId") Long userId);

    @Query("""
          SELECT index.value FROM Index index
          WHERE index.id =
              (SELECT max(i.id) from Index i
              INNER JOIN i.clientCode cc
              INNER JOIN cc.user u
              ON u.id = :userId
              AND cc.branch = :branch)
          """)
    Optional<Double> findLastIndexValue(@Param("branch") Branch branch, @Param("userId") Long userId);

    @Query("""
          SELECT max(i.modifiedAt) from Index i
              INNER JOIN i.clientCode cc
              INNER JOIN cc.user u
              ON u.id = :userId
              AND cc.branch = :branch
          """)
    Optional<Instant> findLastModifiedDate(@Param("branch") Branch branch, @Param("userId") Long userId);

    @Query("""
          SELECT index.createdAt FROM Index index
          WHERE index.id =
              (SELECT max(i.id) from Index i
              INNER JOIN i.clientCode cc
              INNER JOIN cc.user u
              ON u.id = :userId
              AND cc.branch = :branch)
          """)
    Optional<Instant> findLastCreatedDate(@Param("branch") Branch branch, @Param("userId") Long userId);

    @Query("""
          SELECT i FROM Index i
          INNER JOIN i.clientCode cc
          ON cc.branch = :branch
          INNER JOIN cc.user u
          ON u.id = :userId
          AND MONTH(i.createdAt) in (:months)
          ORDER BY i.value ASC
          """)
    List<Index> findMinIndexValues(@Param("branch") Branch branch, @Param("userId") Long userId, @Param("months") List<Integer> months);

    @Query("""
          SELECT i FROM Index i
          INNER JOIN i.clientCode cc
          ON cc.branch = :branch
          INNER JOIN cc.user u
          ON u.id = :userId
          AND MONTH(i.createdAt) in (:months)
          ORDER BY i.value DESC
          """)
    List<Index> findMaxIndexValues(@Param("branch") Branch branch, @Param("userId") Long userId, @Param("months") List<Integer> months);
}

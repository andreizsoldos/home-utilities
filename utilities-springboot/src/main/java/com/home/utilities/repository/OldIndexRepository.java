package com.home.utilities.repository;

import com.home.utilities.entities.OldIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OldIndexRepository extends JpaRepository<OldIndex, Long> {

    @Query("""
          SELECT oldIndex FROM OldIndex oldIndex
          INNER JOIN oldIndex.index i
          ON i.id in (:indexesId)
          ORDER BY oldIndex.modifiedAt DESC
          """)
    List<OldIndex> findByIndexesId(@Param("indexesId") List<Long> indexesId);
}

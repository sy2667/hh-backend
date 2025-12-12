package com.household.backend.repository;

import com.household.backend.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, Integer> {

    @Query("SELECT c FROM CommonCode c WHERE c.groupCode = :groupCode AND c.useYn = 'Y'")
    List<CommonCode> findByGroup(@Param("groupCode") String groupCode);

    @Query("SELECT c FROM CommonCode c WHERE c.groupCode = :groupCode AND c.codeValue = :codeValue")
    Optional<CommonCode> findByGroupAndValue(@Param("groupCode") String groupCode,
                                             @Param("codeValue") String codeValue);

}

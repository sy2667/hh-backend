package com.household.backend.repository;

import com.household.backend.entity.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // 사용자 거래내역 조회
    List<Transaction> findByUser_UserPkAndTransactionDateBetween(Integer userPk, LocalDateTime start, LocalDateTime end, Sort sort);

    // 사용자 연별 거래내역 조회
    List<Transaction> findByUserUserPkAndTransactionDateBetween(Integer userPk,LocalDateTime start, LocalDateTime end);

    // 사용자 연별 거래내역 차트 조회
    List<Transaction> findByUserUserPkAndTransactionDateBetweenAndTransactionType(Integer userPk, LocalDateTime start, LocalDateTime end, String transactionType);
}

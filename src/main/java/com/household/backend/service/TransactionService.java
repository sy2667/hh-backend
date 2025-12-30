package com.household.backend.service;

import com.household.backend.dto.req.TransactionCreate;
import com.household.backend.dto.res.TransactionRes;
import com.household.backend.entity.Transaction;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    // 거래내역 생성
    Transaction createTransaction(Integer userPk, TransactionCreate tx);

    // 거래내역 조회 (ID로)
    Transaction findById(Integer transactionPk);

    // 사용자의 모든 거래내역 조회
    List<TransactionRes> findByUser(Integer userPk, String to, String end, Sort sort);

    // 사용자의 특정 타입 거래내역 조회 (수입 또는 지출)
    List<Transaction> findByUserAndType(Integer userPk, String transactionType, Sort sort);

    // 사용자의 특정 카테고리 거래내역 조회
    List<Transaction> findByUserAndCategory(Integer userPk, Integer categoryPk, Sort sort);

    // 특정 기간 거래내역 조회
    List<Transaction> findByPeriod(Integer userPk, LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    // 특정 월 거래내역 조회
    List<Transaction> findByMonth(Integer userPk, int year, int month, Sort sort);

    // 거래내역 수정
    Transaction updateTransaction(Integer transactionPk, TransactionCreate req);

    // 거래내역 삭제
    void deleteTransaction(Integer transactionPk);

    // 총 수입/지출 계산
    Long sumAmount(Integer userPk, String transactionType);

    // 기간별 총 수입/지출 계산
    Long sumAmountByPeriod(Integer userPk, String transactionType, LocalDateTime startDate, LocalDateTime endDate);

}

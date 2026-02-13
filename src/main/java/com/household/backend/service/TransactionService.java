package com.household.backend.service;

import com.household.backend.dto.req.TransactionCreate;
import com.household.backend.dto.res.TransactionMonthListRes;
import com.household.backend.dto.res.TransactionMonthPieRes;
import com.household.backend.dto.res.TransactionRes;
import com.household.backend.entity.Transaction;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TransactionService {

    // 거래내역 생성
    Transaction createTransaction(Integer userPk, TransactionCreate tx);

    // 거래내역 조회 (ID로)
    Transaction findById(Integer transactionPk);

    // 사용자의 모든 거래내역 조회
    List<TransactionRes> findByUser(Integer userPk, String to, String end, Sort sort);

    // 거래내역 수정
    Transaction updateTransaction(Integer transactionPk, TransactionCreate req);

    // 거래내역 삭제
    void deleteTransaction(Integer transactionPk);

    // 연별 거래내역 조회
    TransactionMonthListRes getMonthTransaction(Integer userPk, String year);

    // 연별 차트데이터 조회
    TransactionMonthPieRes getMonthPieTransaction(Integer userPk, String year, Integer month);
}

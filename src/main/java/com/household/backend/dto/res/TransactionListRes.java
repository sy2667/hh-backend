package com.household.backend.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TransactionListRes {
    private Long totalIncome;                   // 총 수입
    private Long totalExpense;                  // 총 지출
    private Long totalBalance;                  // 수입 - 지출
    private Integer totalCount;                 // 거래 개수
    private List<TransactionRes> transactions;  // 거래목록

    public static TransactionListRes from(List<TransactionRes> txList) {
        long income = txList.stream()
                .filter(t -> t.getTransactionType().equals("1"))
                .mapToLong(TransactionRes::getAmount)
                .sum();

        long expense = txList.stream()
                .filter(t -> t.getTransactionType().equals("2"))
                .mapToLong(TransactionRes::getAmount)
                .sum();

        return TransactionListRes.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .totalBalance(income - expense)
                .totalCount(txList.size())
                .transactions(txList)
                .build();

    }

}

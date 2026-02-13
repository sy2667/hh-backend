package com.household.backend.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Builder
public class TransactionMonthListRes {
  private Long totalIncome;   // 1년 총 수입
  private Long totalExpense;  // 1년 총 지출
  private Long totalBalance;  // 1년 총 잔액
  private Integer totalCount; // 1년 총 거래 수

  private List<TransactionMonthRes> months; // 월별 데이터

  public static TransactionMonthListRes from(List<TransactionRes> txList) {

    Map<Integer, List<TransactionRes>> byMonth = txList.stream()
        .collect(Collectors.groupingBy(t -> t.getTransactionDate().getMonthValue()));

    List<TransactionMonthRes> monthList = IntStream.rangeClosed(1, 12)
        .mapToObj(month -> {
          List<TransactionRes> list = byMonth.getOrDefault(month, List.of());

          long income = list.stream()
              .filter(t -> "1".equals(t.getTransactionType()))
              .mapToLong(TransactionRes::getAmount)
              .sum();

          long expense = list.stream()
              .filter(t -> "2".equals(t.getTransactionType()))
              .mapToLong(TransactionRes::getAmount)
              .sum();

          return TransactionMonthRes.builder()
              .month(month)
              .totalIncome(income)
              .totalExpense(expense)
              .totalBalance(income - expense)
              .totalCount(list.size())
              .build();
        })
        .toList();

    long totalIncome = monthList.stream().mapToLong(TransactionMonthRes::getTotalIncome).sum();
    long totalExpense = monthList.stream().mapToLong(TransactionMonthRes::getTotalExpense).sum();

    return TransactionMonthListRes.builder()
        .totalIncome(totalIncome)
        .totalExpense(totalExpense)
        .totalBalance(totalIncome - totalExpense)
        .totalCount(txList.size())
        .months(monthList)
        .build();
  }
}

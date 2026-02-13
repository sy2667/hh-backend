package com.household.backend.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionMonthRes {
  private Integer month;
  private Long totalIncome;
  private Long totalExpense;
  private Long totalBalance;
  private Integer totalCount;
}

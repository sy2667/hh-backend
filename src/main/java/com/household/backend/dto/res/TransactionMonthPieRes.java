package com.household.backend.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TransactionMonthPieRes {
  private Integer month;              // 1~12
  private Long totalExpense;          // 해당 월 지출 합계
  private List<CategoryAmountRes> categories; // 카테고리별 지출
}

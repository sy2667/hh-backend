package com.household.backend.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryAmountRes {
  private Integer categoryPk;
  private String categoryName;
  private Long amount;
}

package com.household.backend.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionCreate {
    private Integer categoryPk;
    private String transactionType;
    private Long amount;
    private String description;
    private LocalDateTime transactionDate;
}

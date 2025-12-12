package com.household.backend.dto.res;

import com.household.backend.entity.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionRes {
    private Integer transactionPk;
    private Integer categoryPk;
    private String categoryName;
    private String transactionType;
    private Long amount;
    private String description;
    private LocalDateTime transactionDate;

    public static TransactionRes from(Transaction t) {
        return TransactionRes.builder()
                .transactionPk(t.getTransactionPk())
                .categoryPk(t.getCategory().getCategoryPk())
                .categoryName(t.getCategory().getCategoryName())
                .transactionType(t.getTransactionType())
                .amount(t.getAmount())
                .description(t.getDescription())
                .transactionDate(t.getTransactionDate())
                .build();
    }
}

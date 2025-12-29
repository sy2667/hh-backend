package com.household.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionCreate {
    private Integer categoryPk;
    private String transactionType;
    private Long amount;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
}

package com.household.backend.controller;

import com.household.backend.common.AuthUtils;
import com.household.backend.common.SessionUtils;
import com.household.backend.dto.req.TransactionCreate;
import com.household.backend.dto.res.*;
import com.household.backend.entity.Transaction;
import com.household.backend.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionRes> create(@RequestBody TransactionCreate req, @AuthenticationPrincipal Integer userPk) {
        Transaction tx = transactionService.createTransaction(userPk, req);

        return ResponseEntity.ok(TransactionRes.from(tx));
    }

    @GetMapping("/month")
    public ResponseEntity<TransactionMonthListRes> getMonthTransaction(@AuthenticationPrincipal Integer userPk, @RequestParam String year) {
        TransactionMonthListRes res = transactionService.getMonthTransaction(userPk, year);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/month/pie")
    public ResponseEntity<TransactionMonthPieRes> getMonthPieTransaction(@AuthenticationPrincipal Integer userPk, @RequestParam String year, @RequestParam Integer month) {
        TransactionMonthPieRes res = transactionService.getMonthPieTransaction(userPk, year, month);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<TransactionListRes> getTransaction(@RequestParam String to, @RequestParam String end, @RequestParam(defaultValue = "date") String sortBy, @RequestParam(defaultValue = "DESC") Sort.Direction order, @AuthenticationPrincipal Integer userPk) {
        String sortProperty;
        if(sortBy.equals("amount")) {
            sortProperty = "amount";
        } else {
            sortProperty = "transactionDate";
        }

        Sort sort = Sort.by(order, sortProperty);

        List<TransactionRes> txList = transactionService.findByUser(userPk, to, end, sort);
        TransactionListRes res = TransactionListRes.from(txList);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{transactionPk}")
    public ResponseEntity<TransactionRes> getPkToTransaction(@PathVariable Integer transactionPk, @AuthenticationPrincipal Integer userPk) {
        Transaction tx = transactionService.findById(transactionPk);

        TransactionRes res = TransactionRes.from(tx);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{transactionPk}")
    public ResponseEntity<TransactionRes> updateTransaction(@PathVariable Integer transactionPk, @RequestBody TransactionCreate req, @AuthenticationPrincipal Integer userPk) {
        Transaction tx = transactionService.updateTransaction(transactionPk, req);
        TransactionRes res = TransactionRes.from(tx);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{transactionPk}")
    public ResponseEntity<Void> delete(@PathVariable Integer transactionPk, @AuthenticationPrincipal Integer userPk) {

        transactionService.deleteTransaction(transactionPk);
        return ResponseEntity.noContent().build();
    }

}

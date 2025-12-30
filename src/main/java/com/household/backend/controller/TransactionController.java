package com.household.backend.controller;

import com.household.backend.common.SessionUtils;
import com.household.backend.dto.req.TransactionCreate;
import com.household.backend.dto.res.TransactionListRes;
import com.household.backend.dto.res.TransactionRes;
import com.household.backend.entity.Transaction;
import com.household.backend.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionRes> create(@RequestBody TransactionCreate req, HttpSession session) {
        Integer userPk = SessionUtils.getLoginUserPk(session);
        Transaction tx = transactionService.createTransaction(userPk, req);

        return ResponseEntity.ok(TransactionRes.from(tx));
    }

    @GetMapping
    public ResponseEntity<TransactionListRes> getTransaction(HttpSession session, @RequestParam String to, @RequestParam String end, @RequestParam(defaultValue = "date") String sortBy, @RequestParam(defaultValue = "DESC") Sort.Direction order) {
        Integer userPk = SessionUtils.getLoginUserPk(session);

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
    public ResponseEntity<TransactionRes> getPkToTransaction(@PathVariable Integer transactionPk, HttpSession session) {
        SessionUtils.getLoginUserPk(session);
        Transaction tx = transactionService.findById(transactionPk);

        TransactionRes res = TransactionRes.from(tx);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{transactionPk}")
    public ResponseEntity<TransactionRes> updateTransaction(@PathVariable Integer transactionPk, @RequestBody TransactionCreate req, HttpSession session) {
        SessionUtils.getLoginUserPk(session);
        Transaction tx = transactionService.updateTransaction(transactionPk, req);
        TransactionRes res = TransactionRes.from(tx);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{transactionPk}")
    public ResponseEntity<Void> delete(@PathVariable Integer transactionPk, HttpSession session) {
        SessionUtils.getLoginUserPk(session);

        transactionService.deleteTransaction(transactionPk);
        return ResponseEntity.noContent().build();
    }

}

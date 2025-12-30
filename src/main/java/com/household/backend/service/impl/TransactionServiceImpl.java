package com.household.backend.service.impl;

import com.household.backend.dto.req.TransactionCreate;
import com.household.backend.dto.res.TransactionRes;
import com.household.backend.entity.Category;
import com.household.backend.entity.Transaction;
import com.household.backend.entity.User;
import com.household.backend.repository.CategoryRepository;
import com.household.backend.repository.TransactionRepository;
import com.household.backend.repository.UserRepository;
import com.household.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Transaction createTransaction(Integer userPk, TransactionCreate tx) {
        User user = userRepository.findById(userPk).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(tx.getCategoryPk()).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setTransactionType(tx.getTransactionType());
        transaction.setAmount(tx.getAmount());
        transaction.setDescription(tx.getDescription());
        transaction.setTransactionDate(tx.getTransactionDate().atStartOfDay());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionRes> findByUser(Integer userPk, String to, String end, Sort sort) {
        LocalDate toDate = LocalDate.parse(to);
        LocalDate endDate = LocalDate.parse(end);

        LocalDateTime startDt = toDate.atStartOfDay();
        LocalDateTime endDt   = endDate.atTime(LocalTime.MAX);

        return transactionRepository.findByUser_UserPkAndTransactionDateBetween(userPk, startDt, endDt, sort)
                .stream()
                .map(TransactionRes::from)
                .toList();
    }

    @Override
    public List<Transaction> findByUserAndType(Integer userPk, String transactionType, Sort sort) {
        return transactionRepository.findByUserAndType(userPk, transactionType, sort);
    }

    @Override
    public List<Transaction> findByUserAndCategory(Integer userPk, Integer categoryPk, Sort sort) {
        return transactionRepository.findByUserAndCategory(userPk, categoryPk, sort);
    }

    @Override
    public List<Transaction> findByPeriod(Integer userPk, LocalDateTime startDate, LocalDateTime endDate, Sort sort) {
        return transactionRepository.findByPeriod(userPk, startDate, endDate, sort);
    }

    @Override
    public List<Transaction> findByMonth(Integer userPk, int year, int month, Sort sort) {
        return transactionRepository.findByMonth(userPk, year, month, sort);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Integer transactionPk, TransactionCreate req) {
        Transaction transaction = transactionRepository.findById(transactionPk).orElseThrow(() -> new RuntimeException("거래내역을 찾을 수 없습니다."));

        if (req.getCategoryPk() != null) {
            Category category = categoryRepository.findById(req.getCategoryPk()).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
            transaction.setCategory(category);
        }

        if (req.getAmount() != null) {
            transaction.setAmount(req.getAmount());
        }

        if (req.getDescription() != null) {
            transaction.setDescription(req.getDescription());
        }

        if (req.getTransactionDate() != null) {
            transaction.setTransactionDate(req.getTransactionDate().atStartOfDay());
        }

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Integer transactionPk) {
        Transaction transaction = transactionRepository.findById(transactionPk).orElseThrow(() -> new RuntimeException("거래내역을 찾을 수 없습니다."));

        transactionRepository.delete(transaction);
    }

    @Override
    public Long sumAmount(Integer userPk, String transactionType) {
        Long sum = transactionRepository.sumAmount(userPk, transactionType);
        return sum != null ? sum : 0L;
    }

    @Override
    public Long sumAmountByPeriod(Integer userPk, String transactionType, LocalDateTime startDate, LocalDateTime endDate) {
        Long sum = transactionRepository.sumAmountByPeriod(userPk, transactionType, startDate, endDate);
        return sum != null ? sum : 0L;
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction findById(Integer transactionPk) {
        return transactionRepository.findById(transactionPk)
            .orElseThrow(() -> new RuntimeException("거래 내역을 찾을 수 없습니다."));
    }

}

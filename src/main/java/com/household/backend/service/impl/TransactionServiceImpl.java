package com.household.backend.service.impl;

import com.household.backend.dto.req.TransactionCreate;
import com.household.backend.dto.res.CategoryAmountRes;
import com.household.backend.dto.res.TransactionMonthListRes;
import com.household.backend.dto.res.TransactionMonthPieRes;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if (req.getTransactionType() != null) {
            transaction.setTransactionType(req.getTransactionType());
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
    @Transactional(readOnly = true)
    public Transaction findById(Integer transactionPk) {
        return transactionRepository.findById(transactionPk)
            .orElseThrow(() -> new RuntimeException("거래 내역을 찾을 수 없습니다."));
    }

    @Override
    public TransactionMonthListRes getMonthTransaction(Integer userPk, String year) {
        int y = Integer.parseInt(year);

        LocalDateTime start = LocalDateTime.of(y, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(y + 1, 1, 1, 0, 0);

        List<Transaction> txList = transactionRepository
            .findByUserUserPkAndTransactionDateBetween(userPk, start, end);

        List<TransactionRes> resList = txList.stream()
            .map(TransactionRes::from)
            .toList();

        return TransactionMonthListRes.from(resList);
    }

    @Override
    public TransactionMonthPieRes getMonthPieTransaction(Integer userPk, String year, Integer month) {

        int y = Integer.parseInt(year);

        LocalDateTime start = LocalDateTime.of(y, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        // 해당 월 거래 조회(여기서는 지출만 파이로 만든다고 가정)
        List<Transaction> txList = transactionRepository
            .findByUserUserPkAndTransactionDateBetweenAndTransactionType(
                userPk, start, end, "2"
            );

        // 카테고리별 합산
        Map<Integer, List<Transaction>> byCategory = txList.stream()
            .collect(Collectors.groupingBy(t -> t.getCategory().getCategoryPk()));

        List<CategoryAmountRes> categories = byCategory.entrySet().stream()
            .map(e -> {
                List<Transaction> list = e.getValue();
                Transaction first = list.get(0);

                long sum = list.stream().mapToLong(Transaction::getAmount).sum();

                return CategoryAmountRes.builder()
                    .categoryPk(first.getCategory().getCategoryPk())
                    .categoryName(first.getCategory().getCategoryName())
                    .amount(sum)
                    .build();
            })
            .sorted((a, b) -> Long.compare(b.getAmount(), a.getAmount()))
            .toList();

        long totalExpense = categories.stream().mapToLong(CategoryAmountRes::getAmount).sum();

        return TransactionMonthPieRes.builder()
            .month(month)
            .totalExpense(totalExpense)
            .categories(categories)
            .build();
    }

}

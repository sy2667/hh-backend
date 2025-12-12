package com.household.backend.repository;

import com.household.backend.entity.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // 사용자 거래내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.user.userPk = :userPk ")
    List<Transaction> findByUser(@Param("userPk") Integer userPk, Sort sort);

    // 특정 사용자의 특정 타입 거래내역 조회 (수입 또는 지출)
    @Query("SELECT t FROM Transaction t WHERE t.user.userPk = :userPk AND t.transactionType = :type")
    List<Transaction> findByUserAndType(@Param("userPk") Integer userPk, @Param("type") String transactionType, Sort sort);

    // 특정 사용자의 특정 카테고리 거래내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.user.userPk = :userPk AND t.category.categoryPk = :categoryPk")
    List<Transaction> findByUserAndCategory(@Param("userPk") Integer userPk, @Param("categoryPk") Integer categoryPk, Sort sort);

    // 특정 기간 거래내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.user.userPk = :userPk AND t.transactionDate BETWEEN :start AND :end")
    List<Transaction> findByPeriod(@Param("userPk") Integer userPk, @Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate, Sort sort);

    // 특정 사용자의 특정 월 거래내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.user.userPk = :userPk AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    List<Transaction> findByMonth(@Param("userPk") Integer userPk, @Param("year") int year, @Param("month") int month, Sort sort);

    // 특정 사용자의 총 수입 계산
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.userPk = :userPk AND t.transactionType = :type")
    Long sumAmount(@Param("userPk") Integer userPk, @Param("type") String transactionType);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.userPk = :userPk AND t.transactionType = :type AND t.transactionDate BETWEEN :start AND :end")
    Long sumAmountByPeriod(@Param("userPk") Integer userPk, @Param("type") String transactionType, @Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate);

}

package com.household.backend.repository;

import com.household.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query("SELECT c FROM Category c WHERE c.user.userPk = :userPk")
    List<Category> findByUser(@Param("userPk") Integer userPk);

    @Query("SELECT c FROM Category c WHERE c.user.userPk = :userPk AND c.categoryType = :type ORDER BY c.createdAt DESC")
    List<Category> findByUserAndType(@Param("userPk") Integer userPk, @Param("type") String categoryType);


}

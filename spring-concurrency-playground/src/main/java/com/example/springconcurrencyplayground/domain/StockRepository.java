package com.example.springconcurrencyplayground.domain;

import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Modifying
    @Query("update Stock s set s.quantity = s.quantity - 1 where s.id = :id")
    void updateStockById(@Param("id") Long id);
}

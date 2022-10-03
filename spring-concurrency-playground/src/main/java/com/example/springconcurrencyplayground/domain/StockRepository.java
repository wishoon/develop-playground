package com.example.springconcurrencyplayground.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Modifying
    @Query("update Stock s set s.quantity = s.quantity - :quantity where s.id = :id")
    void updateStockById(@Param("id") Long id, @Param("quantity") Long quantity);
}

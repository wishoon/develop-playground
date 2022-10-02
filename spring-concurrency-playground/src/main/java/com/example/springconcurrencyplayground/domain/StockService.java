package com.example.springconcurrencyplayground.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Stock을 Application Level에서 처리
     */
    @Transactional
    public void decreaseOfApplication(final Long id, final Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }

    /**
     * Stock을 DataBase Level에서 처리
     */
    @Transactional
    public void decreaseOfDatabase(final Long id, final Long quantity) {
        stockRepository.updateStockById(id);
    }

    public synchronized void synchronizedDecrease(final Long id, final Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
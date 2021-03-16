package com.exyfi.stocks.market.repository;

import com.exyfi.stocks.market.domain.Stocks;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository()
public interface StocksRepository extends ReactiveCrudRepository<Stocks, Long> {
    Mono<Stocks> findStocksByStocksName(String stocksName);
}

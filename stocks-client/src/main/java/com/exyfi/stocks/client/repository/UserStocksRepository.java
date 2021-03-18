package com.exyfi.stocks.client.repository;

import com.exyfi.stocks.client.domain.UserStocks;
import com.exyfi.stocks.client.domain.projection.StockId;
import com.exyfi.stocks.client.domain.projection.UserStock;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface UserStocksRepository extends ReactiveCrudRepository<UserStocks, Long> {


    Flux<UserStocks> findUserStocksByUserId(Long id);

    @Query("select * from user_stocks where user_id IN " +
            " (select users.id from users where login = :login) " +
            "AND stocks_id IN (select id from stocks where stocks.stocks_name = :stocksName)")
    Mono<UserStocks> findUserStocksByLoginAndStocksName(@Param("login") String login,@Param("stocksName") String stocksName);

    @Query("select user_stocks.counts as count, stocks.stocks_name as stock, market_place_provider,sell_price from user_stocks " +
            " join stocks on user_stocks.stocks_id = stocks.id where user_stocks.user_id = $1")
    Flux<UserStock> findUserStocks(Long uid);

    @Query("select stocks.id from stocks where stocks.stock_name= $1")
    Mono<StockId> findStockIdByStockName(String stockName);
}

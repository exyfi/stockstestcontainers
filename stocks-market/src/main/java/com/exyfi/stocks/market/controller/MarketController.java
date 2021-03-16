package com.exyfi.stocks.market.controller;

import com.exyfi.stocks.market.dto.AddStocksRequestDto;
import com.exyfi.stocks.market.dto.StocksResponseDto;
import com.exyfi.stocks.market.dto.UpdateStocksRequestDto;
import com.exyfi.stocks.market.service.StocksMarketService;
import com.exyfi.stocks.market.dto.BuyStockRequestDto;
import com.exyfi.stocks.market.dto.PaymentResponseDto;
import com.exyfi.stocks.market.dto.UpdateStocksPriceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/market")
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final StocksMarketService stocksMarketService;

    @PostMapping("/add")
    public Mono<StocksResponseDto> addStocks(@RequestHeader("marketId") String marketId, @RequestBody AddStocksRequestDto dto) {
        log.info("accepted request: {}", dto);
        return stocksMarketService.addStocks(dto);
    }

    @PostMapping("/stock/update/count")
    public Mono<StocksResponseDto> updateStocksCount(@RequestHeader("marketId") String marketId, @RequestBody UpdateStocksRequestDto dto) {
        log.info("accepted request: {}", dto);
        return stocksMarketService.updateStocksCount(dto);
    }

    @PostMapping("/stock/update/price")
    public Mono<StocksResponseDto> updateStocksCount(@RequestHeader("marketId") String marketId, @RequestBody UpdateStocksPriceRequestDto dto) {
        log.info("accepted request: {}", dto);
        return stocksMarketService.updateStocksPrice(dto);
    }

    @GetMapping("/stocks")
    public Flux<StocksResponseDto> getAllStocks() {
        log.info("accepted request to get all stocks");
        return stocksMarketService.getAllStocks();
    }

    @PostMapping("/stocks/buy")
    public Mono<PaymentResponseDto> buyStock(@RequestBody BuyStockRequestDto dto) {
        log.info("accepted request: {}", dto);
        return stocksMarketService.buyStock(dto);
    }
}

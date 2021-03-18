package com.exyfi.stocks.market.service;

import com.exyfi.stocks.common.dto.BaseResponse;
import com.exyfi.stocks.common.dto.SellReportResponseDto;
import com.exyfi.stocks.common.dto.UserSellStockRequestDto;
import com.exyfi.stocks.market.domain.Stocks;
import com.exyfi.stocks.market.dto.AddStocksRequestDto;
import com.exyfi.stocks.common.dto.StocksResponseDto;
import com.exyfi.stocks.market.dto.UpdateStocksCountRequestDto;
import com.exyfi.stocks.common.exception.StockCompanyNotFoundException;
import com.exyfi.stocks.common.exception.StocksIllegalRequestException;
import com.exyfi.stocks.market.repository.StocksRepository;
import com.exyfi.stocks.common.dto.BuyStockRequestDto;
import com.exyfi.stocks.common.dto.PaymentResponseDto;
import com.exyfi.stocks.market.dto.UpdateStocksPriceRequestDto;
import com.exyfi.stocks.market.mapper.StocksResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class StocksMarketService {
    private static final BigDecimal EPSILON_ERROR = new BigDecimal("1e-8");

    private final StocksRepository stocksRepository;

    public Mono<StocksResponseDto> addStocks(AddStocksRequestDto dto) {
        try {
            Mono<Stocks> savedStocks = stocksRepository.save(new Stocks(dto.getStocksName(),
                    dto.getMarketplaceProvider(), dto.getCounts(), dto.getStockPrice()));
            return savedStocks.map(StocksResponseMapper::mapToStocksResponse);
        } catch (Exception e) {
            throw new StocksIllegalRequestException(e.getMessage());
        }
    }

    public Mono<StocksResponseDto> updateStocksCount(UpdateStocksCountRequestDto dto) {
        if (dto.getCounts() <= -1) {
            throw new StocksIllegalRequestException("Stock count can't be negative");
        }
        return stocksRepository.findStocksByStocksName(dto.getStocksName())
                .switchIfEmpty(Mono.error(new StockCompanyNotFoundException(String.format("Stock company %s not exist. Please check your request", dto.getStocksName()))))
                .doOnNext(stocks -> {
                    stocks.setCounts(stocks.getCounts() + dto.getCounts());
                    stocksRepository.save(stocks).subscribe();
                })
                .map(StocksResponseMapper::mapToStocksResponse);
    }


    public Mono<StocksResponseDto> updateStocksPrice(UpdateStocksPriceRequestDto dto) {
        if (dto.getNewPrice().signum() <= 0) {
            throw new StocksIllegalRequestException("Stock price can't be zero or negative");
        }
        return stocksRepository.findStocksByStocksName(dto.getStocksName())
                .switchIfEmpty(Mono.error(new StockCompanyNotFoundException(String.format("Stock company %s not exist. Please check your request", dto.getStocksName()))))
                .doOnNext(stocks -> {
                    stocks.setSellPrice(dto.getNewPrice());
                    stocksRepository.save(stocks).subscribe();
                })
                .map(StocksResponseMapper::mapToStocksResponse);
    }

    public Flux<StocksResponseDto> getAllStocks() {
        return stocksRepository.findAll()
                .flatMap(stocks -> Mono.just(StocksResponseMapper.mapToStocksResponse(stocks)));
    }

    @Transactional
    public Mono<PaymentResponseDto> buyStock(BuyStockRequestDto dto) {
        return stocksRepository.findStocksByStocksName(dto.getStockName())
                .switchIfEmpty(Mono.error(new StockCompanyNotFoundException(String.format("Stock company %s not exist. Please check your request", dto.getStockName()))))
                .flatMap(stocks -> {
                    if (stocks.getCounts() < dto.getCount()) {
                        throw new StocksIllegalRequestException(String.format("The number of shares for stock %s on the exchange is less [%s] than the number of shares [%s] in the buy request",
                                dto.getStockName(), dto.getCount(), stocks.getCounts()));
                    }
                    if (stocks.getSellPrice().subtract(dto.getOrderPrice()).abs().compareTo(EPSILON_ERROR) > 0) {
                        throw new StocksIllegalRequestException(String.format("Stock price for %s has changed. Actual price is %s", dto.getStockName(), stocks.getSellPrice()));
                    }
                    stocks.setCounts(stocks.getCounts() - dto.getCount());
                    log.info("saving new stocks count: {}",stocks.getCounts());
                    stocksRepository.save(stocks).subscribe();
                    return Mono.just(stocks);
                })
                .map(stocks -> PaymentResponseDto.builder()
                        .stocksName(dto.getStockName())
                        .stockId(stocks.getId())
                        .stockPrice(dto.getOrderPrice())
                        .totalPrice(stocks.getSellPrice().multiply(BigDecimal.valueOf(dto.getCount())))
                        .count(dto.getCount())
                        .build());
    }

    @Transactional
    public Mono<SellReportResponseDto> sellStocks(UserSellStockRequestDto dto) {
        return stocksRepository.findStocksByStocksName(dto.getStockName())
                .switchIfEmpty(Mono.error(new StockCompanyNotFoundException(String.format("Stock company %s not exist. Please check your request", dto.getStockName()))))
                .doOnNext(stocks -> {
                    if (stocks.getSellPrice().subtract(dto.getOrderPrice()).abs().compareTo(EPSILON_ERROR) < 0) {
                        throw new StocksIllegalRequestException(String.format("Your order price for %s is greater than actual price. Actual price is %s", dto.getStockName(), stocks.getSellPrice()));
                    }
                    stocks.setCounts(stocks.getCounts() + dto.getCount());
                    stocksRepository.save(stocks).subscribe();
                })
                .map(stocks -> SellReportResponseDto.builder()
                        .soldCount(dto.getCount())
                        .soldPrice(dto.getOrderPrice())
                        .stockId(stocks.getId())
                        .stockName(stocks.getStocksName())
                        .build());
    }
}

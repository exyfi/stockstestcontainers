package com.exyfi.stocks.client.service;

import com.exyfi.stocks.client.client.ApiClient;
import com.exyfi.stocks.common.dto.BuyStockRequestDto;
import com.exyfi.stocks.common.dto.PaymentResponseDto;
import com.exyfi.stocks.common.dto.SellReportResponseDto;
import com.exyfi.stocks.common.dto.StocksResponseDto;
import com.exyfi.stocks.common.dto.UserSellStockRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class StocksMarketApiService {

    private final ApiClient apiClient;


    public Mono<PaymentResponseDto> buyStocks(BuyStockRequestDto request) {
        return apiClient.invokePostAPI("/api/v1/market/stocks/buy", PaymentResponseDto.class, request);
    }

    public Mono<SellReportResponseDto> sellStocks(UserSellStockRequestDto request) {
        return apiClient.invokePostAPI("/api/v1/market/stocks/sell", SellReportResponseDto.class, request);
    }

    public Flux<StocksResponseDto> getAllStocks() {
        return apiClient.invokeGetAPI("api/v1/market/stocks", StocksResponseDto.class, null, null, null);
    }
}

package com.exyfi.stocks.market.mapper;

import com.exyfi.stocks.market.domain.Stocks;
import com.exyfi.stocks.market.dto.StocksResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StocksResponseMapper {
    public static StocksResponseDto mapToStocksResponse(Stocks stocks) {
        return StocksResponseDto.builder()
                .stockProvider(stocks.getMarketPlaceProvider())
                .stockPrice(stocks.getSellPrice().toString())
                .stockName(stocks.getStocksName())
                .count(stocks.getCounts())
                .build();
    }
}

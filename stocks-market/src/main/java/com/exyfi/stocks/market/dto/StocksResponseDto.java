package com.exyfi.stocks.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StocksResponseDto {
    private String stockName;
    private String stockProvider;
    private String stockPrice;
    private int count;
}

package com.exyfi.stocks.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateStocksPriceRequestDto {
    private String stocksName;
    private BigDecimal newPrice;
}

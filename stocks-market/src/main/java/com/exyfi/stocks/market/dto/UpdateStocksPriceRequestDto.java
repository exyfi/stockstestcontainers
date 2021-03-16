package com.exyfi.stocks.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateStocksPriceRequestDto {
    private String stocksName;
    private BigDecimal newPrice;
}

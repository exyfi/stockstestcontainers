package com.exyfi.stocks.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BuyStockRequestDto {
    private String stockName;
    private int count;
    private BigDecimal orderPrice;
}

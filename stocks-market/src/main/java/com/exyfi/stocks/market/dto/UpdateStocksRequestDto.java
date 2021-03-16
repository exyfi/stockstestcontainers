package com.exyfi.stocks.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateStocksRequestDto {
    private String stocksName;
    private int counts;
}

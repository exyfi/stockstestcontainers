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
public class SellReportResponseDto {
    private Long stockId;
    private Long userId;
    private String stockName;
    private BigDecimal soldPrice;
    private int soldCount;
}

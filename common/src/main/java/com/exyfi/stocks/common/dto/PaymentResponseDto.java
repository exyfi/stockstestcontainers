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
public class PaymentResponseDto {
    private Long stockId;
    private String stocksName;
    private int count;
    private BigDecimal stockPrice;
    private BigDecimal totalPrice;
}

package com.exyfi.stocks.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStocksResponseDto {
    int count;
    String stock;
    String marketPlaceProvider;
    BigDecimal sellPrice;
}

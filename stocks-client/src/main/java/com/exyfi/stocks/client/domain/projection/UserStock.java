package com.exyfi.stocks.client.domain.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class UserStock {
    int count;
    String stock;
    String marketPlaceProvider;
    BigDecimal sellPrice;
}

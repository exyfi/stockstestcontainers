package com.exyfi.stocks.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMoneyRequestDto {
    private String login;
    private BigDecimal moneyInUsd;
}

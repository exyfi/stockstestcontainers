package com.exyfi.stocks.client.dto;

import com.exyfi.stocks.common.dto.BuyStockRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyStockUserRequestDto {
    private String login;
    private Long id;
    BuyStockRequestDto request;
}

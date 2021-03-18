package com.exyfi.stocks.client.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("user_stocks")
@Data
@AllArgsConstructor
public class UserStocks {

    @Id
    private Long stocksId;
    @NotNull
    private Long userId;
    @NotNull
    private Integer counts;
    @NotNull
    private LocalDateTime updated;
}

package com.exyfi.stocks.market.domain;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Stocks {

    @Id
    private Long id;
    @NotNull
    private String stocksName;

    @NotNull
    private String marketPlaceProvider;

    @NotNull
    private Integer counts;

    @NotNull
    private BigDecimal sellPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStocksName() {
        return stocksName;
    }

    public void setStocksName(String stocksName) {
        this.stocksName = stocksName;
    }

    public String getMarketPlaceProvider() {
        return marketPlaceProvider;
    }

    public void setMarketPlaceProvider(String marketPlaceProvider) {
        this.marketPlaceProvider = marketPlaceProvider;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Stocks() {

    }

    public Stocks(@NotNull String stocksName, @NotNull String marketPlaceProvider, @NotNull Integer counts, @NotNull BigDecimal sellPrice) {
        this.stocksName = stocksName;
        this.marketPlaceProvider = marketPlaceProvider;
        this.counts = counts;
        this.sellPrice = sellPrice;
    }
}

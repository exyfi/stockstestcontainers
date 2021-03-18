package com.exyfi.stocks.client.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("users")
public class User {
    @Id
    private Long id;

    @NotNull
    private String login;
    @NotNull
    private BigDecimal money;

    public User(@NotNull String login, @NotNull BigDecimal money, @NotNull LocalDateTime registered) {
        this.login = login;
        this.money = money;
        this.registered = registered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    @NotNull
    private LocalDateTime registered;
}

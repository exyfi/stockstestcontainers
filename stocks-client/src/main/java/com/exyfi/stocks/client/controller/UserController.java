package com.exyfi.stocks.client.controller;

import com.exyfi.stocks.client.domain.User;
import com.exyfi.stocks.client.domain.UserStocks;
import com.exyfi.stocks.client.dto.AddMoneyRequestDto;
import com.exyfi.stocks.client.dto.BuyStockUserRequestDto;
import com.exyfi.stocks.client.dto.RegisterUserRequestDto;
import com.exyfi.stocks.client.dto.UserInfoDto;
import com.exyfi.stocks.client.domain.projection.UserStock;
import com.exyfi.stocks.client.dto.UserStocksResponseDto;
import com.exyfi.stocks.client.service.UserService;
import com.exyfi.stocks.common.dto.BaseResponse;
import com.exyfi.stocks.common.dto.PaymentResponseDto;
import com.exyfi.stocks.common.dto.StocksResponseDto;
import com.exyfi.stocks.common.dto.UserSellStockRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/stocks/client")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/new")
    public Mono<UserInfoDto> registerUser(@RequestBody RegisterUserRequestDto dto) {
        log.info("accepted request: {}", dto);
        return userService.registerUser(dto);
    }

    @PostMapping("user/add-money")
    public Mono<BaseResponse> addMoneyToAccount(@RequestBody AddMoneyRequestDto dto) {
        log.info("accepted request add money for user with login: {}", dto.getLogin());
        return userService.addMoney(dto);
    }

    @PostMapping("user/buy-stocks")
    public Mono<UserStocks> buyStocks(@RequestBody BuyStockUserRequestDto dto) {
        log.info("accepted request: {}", dto);
        return userService.buyStocks(dto);
    }

    @PostMapping("user/sell-stocks")
    public Mono<BaseResponse> sellStocks(@RequestBody UserSellStockRequestDto dto) {
        log.info("accepted request: {}", dto);
        return userService.sellStocks(dto);
    }

    @GetMapping("user/stocks/all")
    public Flux<UserStocksResponseDto> getUserStocks(@RequestHeader("id") Long uid) {
        log.info("accepted request get user stocks, user id: {}", uid);
        return userService.getUserStocks(uid);
    }

    @GetMapping("/stocks")
    public Flux<StocksResponseDto> getALlStocks(@RequestHeader("id") Long uid) {
        log.info("accepted request to get all available stocks, user id: {}", uid);
        return userService.getAvailableStocks();
    }
}

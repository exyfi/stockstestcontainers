package com.exyfi.stocks.client.service;

import com.exyfi.stocks.client.domain.User;
import com.exyfi.stocks.client.domain.UserStocks;
import com.exyfi.stocks.client.dto.AddMoneyRequestDto;
import com.exyfi.stocks.client.dto.BuyStockUserRequestDto;
import com.exyfi.stocks.client.dto.RegisterUserRequestDto;
import com.exyfi.stocks.client.dto.UserInfoDto;
import com.exyfi.stocks.client.dto.UserStocksResponseDto;
import com.exyfi.stocks.client.repository.UserRepository;
import com.exyfi.stocks.client.repository.UserStocksRepository;
import com.exyfi.stocks.common.dto.BaseResponse;
import com.exyfi.stocks.common.dto.StocksResponseDto;
import com.exyfi.stocks.common.dto.UserSellStockRequestDto;
import com.exyfi.stocks.common.exception.NotEnoughMoneyException;
import com.exyfi.stocks.common.exception.NotEnoughStocksException;
import com.exyfi.stocks.common.exception.StocksIllegalRequestException;
import com.exyfi.stocks.common.exception.UserAlreadyRegisteredException;
import com.exyfi.stocks.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserStocksRepository userStocksRepository;
    private final StocksMarketApiService apiClient;

    public Mono<UserInfoDto> registerUser(RegisterUserRequestDto dto) {
        try {
            return userRepository.save(new User(dto.getLogin(), BigDecimal.ZERO, LocalDateTime.now()))
                    .map(user -> UserInfoDto.builder()
                            .login(user.getLogin())
                            .createTime(user.getRegistered())
                            .build());
        } catch (Exception e) {
            throw new UserAlreadyRegisteredException(String.format("User with provided login %s already exist", dto.getLogin()));
        }
    }

    public Mono<BaseResponse> addMoney(AddMoneyRequestDto dto) {
        if (dto.getMoneyInUsd().signum() <= 0) {
            throw new StocksIllegalRequestException("Replenishment amount can't be zero or negative");
        }
        return userRepository.findByLogin(dto.getLogin())
                .switchIfEmpty(Mono.error(new UserNotFoundException(String.format("User with login %s not exist. Please check your request", dto.getLogin()))))
                .doOnNext(user -> {
                    user.setMoney(user.getMoney().add(dto.getMoneyInUsd()));
                    userRepository.save(user).subscribe();
                })
                .map(user -> new BaseResponse(true, null));
    }

    public Mono<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public Mono<UserStocks> buyStocks(BuyStockUserRequestDto dto) {
        var a = getUserByLogin(dto.getLogin());
        return userRepository.findByLogin(dto.getLogin())
                .switchIfEmpty(Mono.error(new UserNotFoundException(String.format("User with login %s not exist. Please check your request", dto.getLogin()))))
                .flatMap(userAcc -> {
                    BigDecimal totalPrice = dto.getRequest().getOrderPrice().multiply(BigDecimal.valueOf(dto.getRequest().getCount()));
                    if (userAcc.getMoney().compareTo(totalPrice) < 0) {
                        throw new NotEnoughMoneyException("Not enough money to perform operation");
                    }
                    log.info("second step. passed checks");
                    userAcc.setMoney(userAcc.getMoney().subtract(totalPrice));
                    userRepository.save(userAcc).subscribe();
                    log.info("holding money from user account to perform operation");
                    return Mono.just(userAcc);
                })
                .flatMap(user -> apiClient.buyStocks(dto.getRequest()))
                .flatMap(paymentResponse -> {
                    log.info("saving user stocks");
                    return this.userStocksRepository
                            .findUserStocksByLoginAndStocksName(dto.getLogin(), dto.getRequest().getStockName())
                            .map(p -> new UserStocks(paymentResponse.getStockId(), dto.getId(), dto.getRequest().getCount(), LocalDateTime.now()))
                            .map(stocks -> {
                                userStocksRepository.save(stocks).subscribe();
                                log.info("saved new stocks");
                                return stocks;
                            });
                });
    }


    public Flux<UserStocksResponseDto> getUserStocks(Long uid) {
        return userStocksRepository.findUserStocks(uid)
                .map(userStock -> UserStocksResponseDto.builder()
                        .marketPlaceProvider(userStock.getMarketPlaceProvider())
                        .stock(userStock.getStock())
                        .count(userStock.getCount())
                        .sellPrice(userStock.getSellPrice())
                        .build());
    }


    public Flux<StocksResponseDto> getAvailableStocks() {
        return apiClient.getAllStocks();
    }

    @Transactional
    public Mono<BaseResponse> sellStocks(UserSellStockRequestDto dto) {
        return userRepository.findByLogin(dto.getLogin())
                .switchIfEmpty(Mono.error(new UserNotFoundException(String.format("User with login %s not exist. Please check your request", dto.getLogin()))))
                .flatMap(user -> userStocksRepository.findUserStocksByLoginAndStocksName(dto.getLogin(), dto.getStockName())
                        .switchIfEmpty(Mono.error(new StocksIllegalRequestException("Illegal request")))
                        .flatMap(userStocks -> {
                            if (userStocks.getCounts() < dto.getCount()) {
                                throw new NotEnoughStocksException("Not enough stocks");
                            }
                            log.info("I'M HERE");
                            userStocks.setCounts(userStocks.getCounts() - dto.getCount());
                            userStocks.setUpdated(LocalDateTime.now());
                            userStocksRepository.save(userStocks).subscribe();

                            apiClient.sellStocks(dto)
                                    .flatMap(sellReportResponseDto -> {
                                        user.setMoney(user.getMoney().add(sellReportResponseDto.getSoldPrice().multiply(BigDecimal.valueOf(sellReportResponseDto.getSoldCount()))));
                                        userRepository.save(user).block();
                                        return Mono.just(new BaseResponse(true, null));
                                    });
                            return Mono.just(new BaseResponse(true, null));
                        }));
    }
}

package com.exyfi.stocks.client.service;

import com.exyfi.stocks.client.controller.UserController;
import com.exyfi.stocks.client.domain.User;
import com.exyfi.stocks.client.dto.AddMoneyRequestDto;
import com.exyfi.stocks.client.dto.RegisterUserRequestDto;
import com.exyfi.stocks.client.dto.UserInfoDto;
import com.exyfi.stocks.client.repository.UserRepository;
import com.exyfi.stocks.common.dto.BaseResponse;
import com.exyfi.stocks.common.exception.StockCompanyNotFoundException;
import com.exyfi.stocks.common.exception.StocksIllegalRequestException;
import com.exyfi.stocks.common.exception.UserAlreadyRegisteredException;
import com.exyfi.stocks.common.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends AbstractTestContainersInregrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRegistration() {
        RegisterUserRequestDto dto = new RegisterUserRequestDto("kukusik");
        Mono<UserInfoDto> response = userController.registerUser(dto);

        assertNotNull(response);
        assertEquals("kukusik", response.block().getLogin());
    }


    @Test
    public void testRegistrationUserAlreadyExist() {
        RegisterUserRequestDto dto = new RegisterUserRequestDto("kukusik");
        Mono<UserInfoDto> response = userController.registerUser(dto);


        Mono<UserInfoDto> secondResponse = userController.registerUser(dto);

        assertNotNull(secondResponse);
        assertThrows(UserAlreadyRegisteredException.class, secondResponse::block);
    }

    @Test
    public void testAddMoneySuccess() {
        userRepository.save(new User("kukusik", BigDecimal.ZERO, LocalDateTime.now())).subscribe();

        AddMoneyRequestDto dto = AddMoneyRequestDto.builder()
                .login("kukusik")
                .moneyInUsd(BigDecimal.TEN)
                .build();

        Mono<BaseResponse> response = userController.addMoneyToAccount(dto);

        assertNotNull(response);
        assertTrue(response.block().isSuccess());

        assertEquals(userRepository.findByLogin("kukusik").block().getMoney(), BigDecimal.TEN);
    }

    @Test
    public void testAddMoneyUserNotExist() {
        AddMoneyRequestDto dto = AddMoneyRequestDto.builder()
                .login("kukusik")
                .moneyInUsd(BigDecimal.TEN)
                .build();

        Mono<BaseResponse> response = userController.addMoneyToAccount(dto);

        assertNotNull(response);


        assertThrows(UserNotFoundException.class, response::block);
    }

    @Test
    public void testAddMoneyUserIncorrectSum() {
        AddMoneyRequestDto dto = AddMoneyRequestDto.builder()
                .login("kukusik")
                .moneyInUsd(BigDecimal.valueOf(-1))
                .build();

        assertThrows(StocksIllegalRequestException.class, () -> userController.addMoneyToAccount(dto));
    }
}
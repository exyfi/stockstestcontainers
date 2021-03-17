package com.exyfi.stocks.market.service;

import com.exyfi.stocks.common.exception.StockCompanyNotFoundException;
import com.exyfi.stocks.common.exception.StocksIllegalRequestException;
import com.exyfi.stocks.market.domain.Stocks;
import com.exyfi.stocks.market.dto.AddStocksRequestDto;
import com.exyfi.stocks.common.dto.BuyStockRequestDto;
import com.exyfi.stocks.common.dto.PaymentResponseDto;
import com.exyfi.stocks.common.dto.StocksResponseDto;
import com.exyfi.stocks.market.dto.UpdateStocksCountRequestDto;
import com.exyfi.stocks.market.dto.UpdateStocksPriceRequestDto;
import com.exyfi.stocks.market.repository.StocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StocksMarketServiceTest {


    @Mock
    StocksRepository stocksRepository;
    @InjectMocks
    StocksMarketService service;

    PodamFactory podamFactory;

    @BeforeEach
    void setup() {
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    void addStocksSuccess() {
        AddStocksRequestDto dto = AddStocksRequestDto.builder()
                .stocksName("SPCE Virgin")
                .stockPrice(BigDecimal.valueOf(36.3))
                .marketplaceProvider("СПБ Биржа")
                .counts(10)
                .build();

        Stocks expected = new Stocks(dto.getStocksName(), dto.getMarketplaceProvider(), dto.getCounts(), dto.getStockPrice());
        when(stocksRepository.save(any())).thenReturn(Mono.just(expected));

        Mono<StocksResponseDto> actual = service.addStocks(dto);

        assertNotNull(actual);
        assertEquals(expected.getStocksName(), actual.block().getStockName());
        verify(stocksRepository, times(1)).save(any());
    }

    @Test
    void addStockThatAlreadyExist() {
        AddStocksRequestDto dto = AddStocksRequestDto.builder()
                .stocksName("SPCE Virgin")
                .stockPrice(BigDecimal.valueOf(36.3))
                .marketplaceProvider("СПБ Биржа")
                .counts(10)
                .build();

        when(stocksRepository.save(any())).thenThrow(new RuntimeException());

        assertThrows(StocksIllegalRequestException.class, () -> service.addStocks(dto));
    }

    @Test
    void updateStocksIllegalCount() {
        UpdateStocksCountRequestDto dto = UpdateStocksCountRequestDto.builder()
                .stocksName("SPCE Virgin")
                .counts(-1)
                .build();

        assertThrows(StocksIllegalRequestException.class, () -> service.updateStocksCount(dto));
    }

    @Test
    void updateStocksCountForNotExistingCompany() {
        UpdateStocksCountRequestDto dto = UpdateStocksCountRequestDto.builder()
                .stocksName("SPCE Virgin")
                .counts(10)
                .build();

        when(stocksRepository.findStocksByStocksName(dto.getStocksName())).thenReturn(Mono.empty());
        Mono<StocksResponseDto> response = service.updateStocksCount(dto);

        assertThrows(StockCompanyNotFoundException.class, response::block);
    }

    @Test
    void updateStocksIllegalPrice() {
        UpdateStocksPriceRequestDto dto = UpdateStocksPriceRequestDto.builder()
                .stocksName("SPCE Virgin")
                .newPrice(BigDecimal.valueOf(-1))
                .build();

        assertThrows(StocksIllegalRequestException.class, () -> service.updateStocksPrice(dto));
    }

    @Test
    void updateStocksPriceForNotExistingCompany() {
        UpdateStocksPriceRequestDto dto = UpdateStocksPriceRequestDto.builder()
                .stocksName("SPCE Virgin")
                .newPrice(BigDecimal.valueOf(10))
                .build();

        when(stocksRepository.findStocksByStocksName(dto.getStocksName())).thenReturn(Mono.empty());
        Mono<StocksResponseDto> response = service.updateStocksPrice(dto);

        assertThrows(StockCompanyNotFoundException.class, response::block);
    }

    @Test
    void getAllStocks() {
        Stocks expected = new Stocks("SPCE Virgin", "СПБ Биржа", 10, BigDecimal.valueOf(36));
        when(stocksRepository.findAll()).thenReturn(Flux.just(expected));
        Flux<StocksResponseDto> allStocks = service.getAllStocks();

        assertNotNull(allStocks);
        assertEquals(allStocks.blockFirst().getStockName(), expected.getStocksName());
    }

    @Test
    void buyStockCompanyNotExist() {
        BuyStockRequestDto dto = BuyStockRequestDto.builder()
                .stockName("SPCE Virgin")
                .count(10)
                .orderPrice(BigDecimal.valueOf(36))
                .build();

        when(stocksRepository.findStocksByStocksName(dto.getStockName())).thenReturn(Mono.empty());
        Mono<PaymentResponseDto> response = service.buyStock(dto);

        assertThrows(StockCompanyNotFoundException.class, response::block);
    }

    @Test
    void buyStockCompanyAmbiguousCount() {
        BuyStockRequestDto dto = BuyStockRequestDto.builder()
                .stockName("SPCE Virgin")
                .count(1000000000)
                .orderPrice(BigDecimal.valueOf(36))
                .build();
        Stocks expected = new Stocks("SPCE Virgin", "СПБ Биржа", 10, BigDecimal.valueOf(36));

        when(stocksRepository.findStocksByStocksName(dto.getStockName())).thenReturn(Mono.just(expected));

        Mono<PaymentResponseDto> paymentResponseDtoMono = service.buyStock(dto);

        assertThrows(StocksIllegalRequestException.class, paymentResponseDtoMono::block);
    }

    @Test
    void buyStockCompanyForIncorrectPrice() {
        BuyStockRequestDto dto = BuyStockRequestDto.builder()
                .stockName("SPCE Virgin")
                .orderPrice(BigDecimal.valueOf(36))
                .build();
        Stocks expected = new Stocks("SPCE Virgin", "СПБ Биржа", 10, BigDecimal.valueOf(35));

        when(stocksRepository.findStocksByStocksName(dto.getStockName())).thenReturn(Mono.just(expected));

        Mono<PaymentResponseDto> paymentResponseDtoMono = service.buyStock(dto);

        assertThrows(StocksIllegalRequestException.class, paymentResponseDtoMono::block);
    }
}
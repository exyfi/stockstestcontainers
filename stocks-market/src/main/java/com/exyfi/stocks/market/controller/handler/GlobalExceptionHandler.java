package com.exyfi.stocks.market.controller.handler;

import com.exyfi.stocks.common.dto.BaseResponse;
import com.exyfi.stocks.common.dto.Error;
import com.exyfi.stocks.common.exception.StockCompanyNotFoundException;
import com.exyfi.stocks.common.exception.StocksIllegalRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_DEFAULT_CODE = "WRONG_REQUEST";

    @ExceptionHandler(StockCompanyNotFoundException.class)
    public ResponseEntity<BaseResponse> handleExceptiom(StockCompanyNotFoundException ex) {
        return new ResponseEntity<>(new BaseResponse(false, new Error(ERROR_DEFAULT_CODE, ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StocksIllegalRequestException.class)
    public ResponseEntity<BaseResponse> handleExceptiom(StocksIllegalRequestException ex) {
        return new ResponseEntity<>(new BaseResponse(false, new Error(ERROR_DEFAULT_CODE, ex.getMessage())), HttpStatus.BAD_REQUEST);
    }
}

package com.exyfi.stocks.common.exception;

public class NotEnoughStocksException extends RuntimeException{
    public NotEnoughStocksException(String message) {
        super(message);
    }

    public NotEnoughStocksException(String message, Throwable cause) {
        super(message, cause);
    }
}

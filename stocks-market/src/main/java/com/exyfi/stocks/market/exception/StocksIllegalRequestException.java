package com.exyfi.stocks.market.exception;

/**
 * Exception throws in case given invalid properties for stocks.
 */
public class StocksIllegalRequestException extends RuntimeException {
    public StocksIllegalRequestException(String message) {
        super(message);
    }

    public StocksIllegalRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.exyfi.stocks.common.exception;

public class NotEnoughMoneyException  extends RuntimeException{
    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}

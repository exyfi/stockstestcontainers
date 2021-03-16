package com.exyfi.stocks.market.exception;

/**
 * Exception throws in case company not found in dao.
 */
public class StockCompanyNotFoundException extends RuntimeException {
    public StockCompanyNotFoundException(String message) {
        super(message);
    }

    public StockCompanyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

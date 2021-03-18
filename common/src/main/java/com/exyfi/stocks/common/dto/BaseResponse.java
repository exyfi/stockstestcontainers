package com.exyfi.stocks.common.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BaseResponse {
    private boolean success;
    private Error error;

    public BaseResponse(boolean success, Error error) {
        this.success = success;
        this.error = error;
    }
}

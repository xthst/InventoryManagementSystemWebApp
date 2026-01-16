package com.xthst.ims.domain.dto.error;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private int status;
    private String message;
    private long timestamp;
    private Map<String, String> errors;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ApiError(int status, String message, Map<String, String> errors) {
        this(status, message);
        this.errors = errors;
    }
}

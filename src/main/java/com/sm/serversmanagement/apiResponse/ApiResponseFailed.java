package com.sm.serversmanagement.apiResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseFailed {

    @JsonFormat(pattern = "dd-MM-YYY HH:mm:ss")
    private LocalDateTime time;
    private String field;
    private String message;
    private HttpStatus status;
    private int statusCode;

}

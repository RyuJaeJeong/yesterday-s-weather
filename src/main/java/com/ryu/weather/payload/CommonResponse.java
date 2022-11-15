package com.ryu.weather.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<E> {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private int count;
    private E data;
}

package com.medic.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response<T> {

    private T payload;
    private String timestamp;

    public Response(T data){
        payload = data;
        timestamp = LocalDateTime.now().toString();
    }

}

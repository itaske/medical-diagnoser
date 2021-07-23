package com.medic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.TemporalType;
import java.time.Instant;
import java.time.temporal.TemporalUnit;

@Data
public class Token {

    @JsonProperty("Token")
    private String token;

    @JsonProperty("ValidThrough")
    private long validThrough;

    private Instant createdOn;



    public boolean hasExpired(){
        if (token== null)
            return true;
        if (createdOn == null)
            return true;
        if (validThrough == 0)
            return true;
        return createdOn.plusSeconds(validThrough).isBefore(Instant.now());
    }

}

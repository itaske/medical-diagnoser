package com.medic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;


@Data
public class Symptom {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;


    @Override
    public String toString(){
        return String.format("%d:%s", id, name);
    }
}

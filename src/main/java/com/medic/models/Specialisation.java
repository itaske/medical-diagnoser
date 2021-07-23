package com.medic.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Specialisation {
    
    @Id
    @JsonProperty("ID")
    @GeneratedValue
    private Integer id;

   @JsonProperty("Name")
   private String name;

   @JsonProperty("SpecialisationID")
   private Integer specialisationId;

    @OneToOne
    @JsonIgnore
    private Specialist specialist;
}

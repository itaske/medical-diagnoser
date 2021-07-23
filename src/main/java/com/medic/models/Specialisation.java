package com.medic.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Data
@Entity
public class Specialisation {
    
    @Id
    @JsonProperty("ID")
    private Integer id;

   @JsonProperty("Name")
   private String name;

   @JsonProperty("SpecialisationID")
   private Integer specialisationId;

    @OneToOne
    @JsonIgnore
    private Specialist specialist;
}

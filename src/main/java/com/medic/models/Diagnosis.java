package com.medic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "diagnosis")
@Data
public class Diagnosis {


    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("Issue")
    @Embedded
    private Issue issue;

    @JsonProperty("Specialisation")
    @OneToMany
    private List<Specialisation> specialisation;

    private Boolean valid;

}

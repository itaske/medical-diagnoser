package com.medic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class Issue implements Serializable {

      @JsonProperty("ID")
      @Column(insertable = false, updatable = false)
      private Long id;

      @JsonProperty("Name")
      private String name;

      @JsonProperty("Accuracy")
      private Double accuracy;

      @JsonProperty("Icd")
      private String icd;

      @JsonProperty("IcdName")
      private String icdName;

      @JsonProperty("ProfName")
      private String profName;

      @JsonProperty("Ranking")
      private int ranking;
}

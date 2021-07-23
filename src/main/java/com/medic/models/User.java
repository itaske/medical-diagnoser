package com.medic.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

  @Id
  private Long id;

  private String email;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private int yearOfBirth;

  @OneToMany
  @JsonIgnore
  private List<Diagnosis> diagnosisHistory;

  public enum Gender{
    male,
    female
  }
}

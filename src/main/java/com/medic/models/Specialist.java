package com.medic.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper=false)
public class Specialist extends User{

    private String institutionName;
}

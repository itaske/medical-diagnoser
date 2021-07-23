package com.medic.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name= "diagnosis_records")
@Data
public class DiagnosisRecord {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    private Diagnosis diagnosis;

    private boolean valid;

}

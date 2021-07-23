package com.medic.models;

import com.medic.converters.SymptomsListConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "records")
@Data
public class Record {

    @Id
    @GeneratedValue
    private Long id;


    @OneToMany
    private List<DiagnosisRecord> diagnosesRecord;


    @Convert(converter = SymptomsListConverter.class)
    private List<Symptom> symptoms;


    @ManyToOne
    private User user;
}

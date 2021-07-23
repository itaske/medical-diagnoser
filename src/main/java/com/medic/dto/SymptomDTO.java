package com.medic.dto;

import com.medic.models.Symptom;
import com.medic.models.User;
import lombok.Data;

import java.util.List;

@Data
public class SymptomDTO {

    private List<Symptom> symptoms;

    private User.Gender gender;

    private int yearOfBirth;
}

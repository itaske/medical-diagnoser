package com.medic.converters;

import com.medic.models.Symptom;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SymptomsListConverter implements AttributeConverter<List<Symptom>, String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(List<Symptom> attributes) {
        StringBuilder builder = new StringBuilder();
        if (attributes == null){
            return builder.toString();
        }

        StringJoiner joiner = new StringJoiner(SEPARATOR);
        attributes.stream().forEach(e->joiner.add(e.toString()));

        return joiner.toString();
    }

    @Override
    public List<Symptom> convertToEntityAttribute(String dbData) {
        String values[] = dbData.split(SEPARATOR);
        List<Symptom> list = new ArrayList();
        for (String s: values){
           String properties[] = s.split(":");
           Symptom symptom = new Symptom();
           symptom.setId(Integer.parseInt(properties[0]));
           symptom.setName(properties[1]);
            list.add(symptom);
        }
        return list;
    }
}

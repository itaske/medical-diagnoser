package com.medic.services;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medic.configs.MedicConfig;
import com.medic.dto.SymptomDTO;
import com.medic.dto.Token;
import com.medic.models.*;
import com.medic.respositories.DiagnosisRepository;
import com.medic.respositories.SpecialisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicRestfulService {

    @Autowired
    private MedicConfig medicConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Token token;

    @Autowired
    DiagnosisRepository diagnosisRepository;

    @Autowired
    SpecialisationRepository specialisationRepository;


    /**
     * Get the Available Symptoms on the System
     * @return the List of Symptoms
     */
    @Cacheable(value = "symptomsCache")
    public List<Symptom> getAvailableSymptoms(){
       System.out.println("Entered here to get symptoms");
        String accessToken = accessToken().getToken();

        String formattedURL = String.format("%s&token=%s", medicConfig.getSymptomsURL(), accessToken);
        List<Symptom> response = restTemplate.getForObject(formattedURL, ArrayList.class);

        return response;

    }

    /**
     * Get All the Diagnosis in the System
     * @param size the number of records to return
     * @param currentPage the current page of the records
     * @return
     */
    public List<Diagnosis> getAllDiagnosis(int size, int currentPage){
        Pageable pageable = PageRequest.of(currentPage, size);
        return diagnosisRepository.findAll(pageable).getContent();
    }


    /**
     * Diagnose Symptoms
     * @param symptomDTO
     * @return the List of Diagnosis as result of the symptoms
     */
    public List<Diagnosis> diagnose(SymptomDTO symptomDTO){
        String accessToken = accessToken().getToken();
        String symptoms = symptomDTO.getSymptoms().stream().map(symptom-> String.valueOf(symptom.getId()))
                .collect(Collectors.joining(",", "[", "]"));
        String formattedURL = String.format("%s&gender=%s&year_of_birth=%s&symptoms=%s&token=%s", medicConfig.getDiagnosisURL(),
                symptomDTO.getGender().toString(), symptomDTO.getYearOfBirth(), symptoms, accessToken);

        String response = restTemplate.getForObject(formattedURL, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JsonNode jsonParser = null;
        try {
            jsonParser = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Diagnosis> diagnosisList = new ArrayList<>();
        for (Iterator<JsonNode> it = jsonParser.elements(); it.hasNext(); ) {
            JsonNode node = it.next();
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setIssue(objectMapper.convertValue(node.get("Issue"), Issue.class));
            List<Specialisation> specialisations = new ArrayList<>();
            for(JsonNode n: node.get("Specialisation")){
                Specialisation s = objectMapper.convertValue(n, Specialisation.class);
                s = specialisationRepository.save(s);
                specialisations.add(s);
            }
//            diagnosis.setSpecialisation(specialisations);
            diagnosis.setSpecialisation(null);
            diagnosis.setValid(false);
            diagnosisList.add(diagnosis);
        }
        diagnosisList = diagnosisRepository.saveAll(diagnosisList);
        return diagnosisList;

    }


    public List<Diagnosis> updateValidDiagnosis(List<Diagnosis> validDiagnosis){
        List<Diagnosis> diagnoses = diagnosisRepository.findAllById(validDiagnosis.stream()
        .map(d->d.getId()).collect(Collectors.toList()));

        diagnoses =diagnoses.stream().map(d->{d.setValid(true); return d;}).collect(Collectors.toList());

        return diagnosisRepository.saveAllAndFlush(diagnoses);
    }

    /**
     * Get the Access Token of the Medic Api
     * @return token
     */
    public Token accessToken(){
        if (token.hasExpired()){
            HttpHeaders headers = new HttpHeaders();
            String bearerToken = bearerToken();
            headers.setBearerAuth(bearerToken);

            HttpEntity<Void> entity = new HttpEntity<>(null, headers);
            ResponseEntity<Token> response = restTemplate.postForEntity(medicConfig.getLoginURL(), entity, Token.class);
            token.setToken(response.getBody().getToken());
            token.setValidThrough(response.getBody().getValidThrough());
            token.setCreatedOn(Instant.now());
        }
        return token;
    }


    /**
     * Calculate the Bearer Token
     * @return bearerToken
     */
    private String bearerToken(){
        System.out.println(medicConfig);
        byte[] secretBytes = medicConfig.getSecretKey().getBytes(StandardCharsets.UTF_8);
        String computedHashString = null;
        try {
            computedHashString = generateHmac256(medicConfig.getLoginURL(), secretBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.format("%s:%s",medicConfig.getApiKey(), computedHashString);
    }

    String generateHmac256(String message, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] bytes = hmac("HMACMD5", key, message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }



}

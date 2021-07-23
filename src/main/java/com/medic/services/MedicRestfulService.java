package com.medic.services;


import com.medic.configs.MedicConfig;
import com.medic.dto.SymptomDTO;
import com.medic.dto.Token;
import com.medic.models.Diagnosis;
import com.medic.models.Record;
import com.medic.models.Symptom;
import com.medic.respositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
    RecordRepository recordRepository;


    @Cacheable(value = "symptomsCache")
    public List<Symptom> getAvailableSymptoms(){
       System.out.println("Entered here to get symptoms");
        String accessToken = accessToken().getToken();

        String formattedURL = String.format("%s&token=%s", medicConfig.getSymptomsURL(), accessToken);
        List<Symptom> response = restTemplate.getForObject(formattedURL, ArrayList.class);

        return response;

    }

    public List<Diagnosis> diagnose(SymptomDTO symptomDTO){
        String accessToken = accessToken().getToken();
        String symptoms = symptomDTO.getSymptoms().stream().map(symptom-> String.valueOf(symptom.getId()))
                .collect(Collectors.joining(",", "[", "]"));
        String formattedURL = String.format("%s&gender=%s&year_of_birth=%s&symptoms=%s&token=%s", medicConfig.getDiagnosisURL(),
                symptomDTO.getGender().toString(), symptomDTO.getYearOfBirth(), symptoms, accessToken);

        List<Diagnosis> diagnoses = restTemplate.getForObject(formattedURL, ArrayList.class);
        return diagnoses;

    }


    public Record rateDiagnosis(com.medic.models.Record record){
        Record savedRecord = recordRepository.save(record);
        return savedRecord;
    }
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

package com.medic.controller;

import com.medic.dto.SymptomDTO;
import com.medic.services.MedicRestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/medic")
@RestController
public class MedicController {

    @Autowired
    private MedicRestfulService medicRestfulService;

    @GetMapping("/symptoms")
    public ResponseEntity<?> getSymptoms(){
        return ResponseEntity.ok(medicRestfulService.getAvailableSymptoms());
    }

    @PostMapping("/diagnosis")
    public ResponseEntity<?> diagnoseSymptoms(@RequestBody SymptomDTO symptomDTO){
        return ResponseEntity.ok(medicRestfulService.diagnose(symptomDTO));
    }

    @PostMapping("/rate-diagnosis")
    public ResponseEntity<?> rateDiagnosis(@RequestBody com.medic.models.Record record){
      return ResponseEntity.ok(medicRestfulService.rateDiagnosis(record));
    }

}

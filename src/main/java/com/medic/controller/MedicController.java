package com.medic.controller;

import com.medic.dto.SymptomDTO;
import com.medic.models.Diagnosis;
import com.medic.services.MedicRestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/medic")
@RestController
public class MedicController {

    @Autowired
    private MedicRestfulService medicRestfulService;

    @GetMapping("/symptoms")
    public ResponseEntity<?> getSymptoms(){
        return ResponseEntity.ok(medicRestfulService.getAvailableSymptoms());
    }

    @GetMapping("/diagnosis")
    public ResponseEntity<?> getAllDiagnosis(@RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                             @RequestParam(value="currentPage", defaultValue = "0", required = false) int currentPage){
        return ResponseEntity.ok(medicRestfulService.getAllDiagnosis(size, currentPage));
    }

    @PostMapping("/diagnosis")
    public ResponseEntity<?> diagnoseSymptoms(@RequestBody SymptomDTO symptomDTO){
        return ResponseEntity.ok(medicRestfulService.diagnose(symptomDTO));
    }

    @PostMapping("/validate-diagnosis")
    public ResponseEntity<?> markDiagnosisAsValid(@RequestBody List<Diagnosis> diagnosis){
      return ResponseEntity.ok(medicRestfulService.updateValidDiagnosis(diagnosis));
    }

}

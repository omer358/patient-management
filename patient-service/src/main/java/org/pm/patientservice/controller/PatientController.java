package org.pm.patientservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pm.patientservice.dto.PatientRequestDTO;
import org.pm.patientservice.dto.PatientResponseDto;
import org.pm.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @PostMapping("/add")
    public ResponseEntity<PatientResponseDto> createNewPatient(
            @Valid @RequestBody PatientRequestDTO patientRequestDTO
            ){
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }
}

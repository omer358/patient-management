package org.pm.patientservice.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.pm.patientservice.dto.CreatePatientValidationGroup;
import org.pm.patientservice.dto.PatientRequestDTO;
import org.pm.patientservice.dto.PatientResponseDto;
import org.pm.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody
            PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable("id") UUID id,
            @Validated({Default.class})
            @RequestBody
            PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }
}

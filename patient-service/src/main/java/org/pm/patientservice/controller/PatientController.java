package org.pm.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Patient Service"
        , description = "An Api for managing patients"
)
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new patient")
    public ResponseEntity<PatientResponseDto> createNewPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody
            PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable("id") UUID id,
            @Validated({Default.class})
            @RequestBody
            PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing patient")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}

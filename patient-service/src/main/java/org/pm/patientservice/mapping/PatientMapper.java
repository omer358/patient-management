package org.pm.patientservice.mapping;

import org.pm.patientservice.dto.PatientRequestDTO;
import org.pm.patientservice.dto.PatientResponseDto;
import org.pm.patientservice.model.Patient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PatientMapper {

    public PatientResponseDto toDto(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .address(patient.getAddress())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public Patient toEntity(PatientRequestDTO patientRequestDTO) {
        return Patient.builder()
                .name(patientRequestDTO.getName())
                .address(patientRequestDTO.getAddress())
                .email(patientRequestDTO.getEmail())
                .dateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()))
                .registeredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()))
                .build();
    }
}

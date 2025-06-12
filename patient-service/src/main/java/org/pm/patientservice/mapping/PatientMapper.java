package org.pm.patientservice.mapping;

import org.pm.patientservice.dto.PatientResponseDto;
import org.pm.patientservice.model.Patient;
import org.springframework.stereotype.Component;

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
}

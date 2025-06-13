package org.pm.patientservice.service;

import lombok.RequiredArgsConstructor;
import org.pm.patientservice.dto.PatientRequestDTO;
import org.pm.patientservice.dto.PatientResponseDto;
import org.pm.patientservice.exception.EmailAlreadyExistsException;
import org.pm.patientservice.mapping.PatientMapper;
import org.pm.patientservice.model.Patient;
import org.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(patientMapper::toDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDTO.getEmail());
        }
        Patient patient = patientMapper.toEntity(patientRequestDTO);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }
}

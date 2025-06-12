package org.pm.patientservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientResponseDto {
    private String id;
    private String name;
    private String address;
    private String email;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
}

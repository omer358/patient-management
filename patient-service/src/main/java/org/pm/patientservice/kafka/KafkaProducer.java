package org.pm.patientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.pm.patientservice.model.Patient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.event.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendEvent(Patient patient) {
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try{
            kafkaTemplate.send("patient", patientEvent.toByteArray());
            log.info("Sent patient event to kafka: {}", patientEvent);
        }catch (Exception e){
            log.error("Error while sending patient event to kafka: {}", e.getMessage());
        }
    }
}

package org.pm.patientservice.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}

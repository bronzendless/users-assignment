package dev.bron.assignment.users_assignment.exception;

public class CustomerAlreadyExistsException extends BaseUncheckedException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}

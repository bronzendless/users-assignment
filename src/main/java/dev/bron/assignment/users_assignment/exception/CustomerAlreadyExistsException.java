package dev.bron.assignment.users_assignment.exception;

public class CustomerAlreadyExistsException extends BaseCheckedException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}

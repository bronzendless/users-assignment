package dev.bron.assignment.users_assignment.exception;

public class CustomerNotFoundException extends BaseCheckedException {
    public CustomerNotFoundException(String field) {
        super("Customer with " + field + " not found");
    }
}

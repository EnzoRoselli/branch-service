package tesis.company.exceptions;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) { super(message); }
}

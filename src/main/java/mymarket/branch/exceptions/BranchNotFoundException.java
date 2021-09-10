package mymarket.branch.exceptions;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) { super(message); }
}

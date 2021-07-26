package util;

public class NegativeInputException extends Exception {
    private String message;

    public NegativeInputException() {
    }

    public NegativeInputException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "A positive input is expected here!";
    }
}

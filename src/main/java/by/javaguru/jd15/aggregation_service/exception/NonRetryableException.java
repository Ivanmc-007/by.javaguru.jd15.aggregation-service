package by.javaguru.jd15.aggregation_service.exception;

public class NonRetryableException extends RuntimeException {
    public NonRetryableException() {
    }

    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}

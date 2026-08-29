package sol.auth.core.exception;

public class PasswordReuseException extends RuntimeException {

    public PasswordReuseException(String message) {
        super(message);

    }

}

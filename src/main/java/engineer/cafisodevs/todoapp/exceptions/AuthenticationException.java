package engineer.cafisodevs.todoapp.exceptions;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("An error encountered while performing the authentication");
    }


    public AuthenticationException(String exception) {
        super(exception);
    }

}

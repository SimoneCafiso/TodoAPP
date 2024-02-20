package engineer.cafisodevs.todoapp.exceptions;

public class EmailAlreadyInUseException extends RuntimeException    {
    public EmailAlreadyInUseException() {
        super("It seems like the email you're trying to use is already in use. Please try again with a different email. Or login with the existing account.");
    }

    public EmailAlreadyInUseException(String message) {
        super(message);
    }

}

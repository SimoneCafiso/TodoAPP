package engineer.cafisodevs.todoapp.exceptions;

public class AccountNotFoundException extends RuntimeException  {
    public AccountNotFoundException() {
        super("It seems like the account you're trying to gather does not exist. Please try double checking the information.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}

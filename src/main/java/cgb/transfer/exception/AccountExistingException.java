package cgb.transfer.exception;

public class AccountExistingException extends Exception {

	public AccountExistingException() {
		super("An account with this IBAN already exists in the database");
	}
	
}

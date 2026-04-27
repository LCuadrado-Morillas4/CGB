package cgb.transfer.exception;

/**
 * Exception levée lorsque l'IBAN du compte renseigné est introuvable
 */
public class InvalidAccountException extends Exception {

	public InvalidAccountException(String account) {
		super(account + " account doesn't exist");
	}
	
}

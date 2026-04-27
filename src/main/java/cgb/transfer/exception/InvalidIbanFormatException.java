package cgb.transfer.exception;

/**
 * Exception levée lorsque le format de l'IBAN du compte renseigné est invalide
 */
public class InvalidIbanFormatException extends InvalidIbanException{

	public InvalidIbanFormatException() {
		super("IBAN Format is Invalid");
	}
	
}

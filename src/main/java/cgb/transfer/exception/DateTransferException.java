package cgb.transfer.exception;

/**
 * Exception levée lorsque la date renseignée dans le transfert est antérieure à la date d'aujourd'hui
 */
public class DateTransferException extends Exception {

	public DateTransferException() {
		super("Transfer date prior to today");
	}
	
}

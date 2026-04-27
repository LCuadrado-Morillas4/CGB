package cgb.transfer.exception;

/**
 * Exception levée lorsque le montant du transfert est supérieur au solde du compte source
 */
public class InsufficientFundsException extends Exception {

	public InsufficientFundsException() {
		super("Insufficient funds for source account");
	}
	
}

package cgb.transfer.exception;

/**
 * Exception levée lorsque le montant du transfert est négatif
 */
public class NegativeTransferAmountException extends Exception {

	public NegativeTransferAmountException() {
		super("Transfer amount can't be negative");
	}
	
}

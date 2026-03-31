package cgb.transfer.exception;

public class NegativeTransferAmountException extends Exception {

	public NegativeTransferAmountException() {
		super("Transfer amount can't be negative");
	}
	
}

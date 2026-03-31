package cgb.transfer.exception;

public class InsufficientFundsException extends Exception {

	public InsufficientFundsException() {
		super("Transfer Date prior to today");
	}
	
}

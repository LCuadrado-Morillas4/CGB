package cgb.transfer.exception;

public class InsufficientFundsException extends Exception {

	public InsufficientFundsException() {
		super("Insufficient funds for source account");
	}
	
}

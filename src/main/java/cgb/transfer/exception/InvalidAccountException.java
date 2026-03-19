package cgb.transfer.exception;

public class InvalidAccountException extends Exception {

	public InvalidAccountException(String account) {
		super(account + " account doesn't exist");
	}
	
}

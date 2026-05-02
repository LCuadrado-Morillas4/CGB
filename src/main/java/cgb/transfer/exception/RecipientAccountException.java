package cgb.transfer.exception;

public class RecipientAccountException extends Exception {

	public RecipientAccountException(String srcAccNumber, String destAccNumber) {
		super("Account " + destAccNumber + " is not a registered beneficiary of account " + srcAccNumber);
	}
	
}

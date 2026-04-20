package cgb.transfer.exception;

public class DateTransferException extends Exception {

	public DateTransferException() {
		super("Transfer date prior to today");
	}
	
}

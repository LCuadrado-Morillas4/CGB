package cgb.transfer.exception;

/**
 * Classe d'exception lancée lors d'une erreur de suppression d'un compte.
 */
public class DeleteAccountException extends Exception {
	private static final long serialVersionUID = 1L;

	public enum FailureAccount{
			OBJECT_NOT_FOUND,
			REMOVAL_FAILURE,
			BALANCE_NOT_NULL,
			ACCOUNT_SENT_TRANSFERS
	};
	
	public  DeleteAccountException(FailureAccount fa) {
		// TODO Auto-generated constructor stub
		super(fa.name());
	}
	
}

package cgb.utils;

import org.apache.commons.validator.routines.IBANValidator;

import cgb.transfer.exception.InvalidIbanException;
import cgb.transfer.exception.InvalidIbanFormatException;
import cgb.transfer.exception.InvalidUnCheckableIbanException;

public class CGBIbanValidator {

	private static CGBIbanValidator instance;
	
	private CGBIbanValidator() {
		
	}
	
	public static CGBIbanValidator getInstance() {
		if (instance == null) {
			return new CGBIbanValidator();
		}
		return instance;
	}
	
	public boolean isIbanStructureValid(String iban) throws InvalidIbanFormatException {
		return iban.matches("FR{2}[0-9]{25}");
	}
	
	public boolean isIbanValid(String iban) {
		IBANValidator validator = IBANValidator.getInstance();
		return validator.isValid(iban);
	}
	
	public String getCountryCode(String iban) {
		return iban.substring(0, 1);
	}
	
	public String getCheckDigits(String iban) {
		return iban.substring(2, 3);
	}
	
	public String getBBAN(String iban) {
		return iban.substring(4);
	}
	
}

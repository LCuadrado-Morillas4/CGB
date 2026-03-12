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
		for (int i = 0; i < iban.length(); i++) {
			if (i <= 1) {
				if (!Character.isLetter(iban.charAt(i))) {throw new InvalidIbanFormatException();}
			}
			else if (i <= 3) {
				if (!Character.isDigit(iban.charAt(i))) {throw new InvalidIbanFormatException();}
			}
			else {
				if (!Character.isDigit(iban.charAt(i))) {throw new InvalidIbanFormatException();}
			}
		}
		return true;
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

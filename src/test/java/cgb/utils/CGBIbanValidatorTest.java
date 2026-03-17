package cgb.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import cgb.transfer.exception.InvalidIbanFormatException;

public class CGBIbanValidatorTest {
	
	public static CGBIbanValidator ibanValidator;
	
	@BeforeAll
	public static void oneTimeSetUp() {
		System.out.println("BeforeAll - CGBIbanValidatorTest");
		ibanValidator = CGBIbanValidator.getInstance();
	}
	
	@Test
	public void testGetInstance() {		
		assertNotNull(ibanValidator);
		assertInstanceOf(CGBIbanValidator.class, ibanValidator);
	}
	
	@Test
	public void testIsIbanStructureValid() throws InvalidIbanFormatException {
		assertTrue(ibanValidator.isIbanStructureValid("FR2341201815585586861823448"));
		assertFalse(ibanValidator.isIbanStructureValid("FR"));
		assertFalse(ibanValidator.isIbanStructureValid("2341201815585586861823448"));
		assertFalse(ibanValidator.isIbanStructureValid("LBJ23"));		
	}
	
	@Test
	public void testIsIbanValid() {
		assertTrue(ibanValidator.isIbanValid("FR2341201815585586861823448"));
		assertFalse(ibanValidator.isIbanValid("FR"));
		assertFalse(ibanValidator.isIbanValid("2341201815585586861823448"));
		assertFalse(ibanValidator.isIbanValid("LBJ23"));	
	}
	
	@Test
	public void testGetCountryCode() {
		assertEquals("FR", ibanValidator.getCountryCode("FR2341201815585586861823448"));
		assertNotEquals("EN", ibanValidator.getCountryCode("FR2341201815585586861823448"));
		assertNotEquals("23", ibanValidator.getCountryCode("FR2341201815585586861823448"));
	}
	
	@Test
	public void testGetCheckDigits() {
		assertEquals("23", ibanValidator.getCheckDigits("FR2341201815585586861823448"));
		assertNotEquals("EN", ibanValidator.getCheckDigits("FR2341201815585586861823448"));
		assertNotEquals("44", ibanValidator.getCheckDigits("FR2341201815585586861823448"));	
	}
	
	@Test
	public void testGetBBAN() {
		assertEquals("41201815585586861823448", ibanValidator.getBBAN("FR2341201815585586861823448"));
		assertNotEquals("EN", ibanValidator.getBBAN("FR2341201815585586861823448"));
		assertNotEquals("44", ibanValidator.getBBAN("FR2341201815585586861823448"));	
	}

}